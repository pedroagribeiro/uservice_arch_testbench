import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeoutException;

public class App {

    @Parameter(names = { "-containerized" }, description = "Describes if the running environment if fully containerized")
    private static boolean containerized;

    private static long message_timeout = 20000;
    private int message_id = 0;
    private boolean on_going_run = false;
    private static Gson converter = new Gson();

    private String queue_host;
    private int queue_port;
    private String producer_orchestration_queue_host;
    private int producer_orchestration_queue_port;
    private String results_database_host;
    private int results_database_port;
    private String redis_database_host;
    private int redis_database_port;

    private JedisPool redis_database_pool;
    private JedisPool results_database_pool;
    private Connection broker_queue_connection;
    private Connection orchestration_queue_connection;
    private Channel orchestration_queue_orchestration_channel;
    private Channel broker_queue_messaging_channel;
    private Channel broker_queue_orchestrationg_channel;
    
    Logger log = LoggerFactory.getLogger(this.getClass().getName());

    public static void main(String[] args) throws Exception {
        App application = new App();
        JCommander commands = JCommander.newBuilder().addObject(application).build();
        try {
            commands.parse(args);
        } catch (Exception e) {
            commands.usage();
            System.exit(-1);
        }
        application.run();
    }

    public void run() {
        establish_environment_variables();
        establish_connection_with_results_database();
        establish_connection_with_redis_database();
        establish_connection_with_broker_queue();
        establish_broker_queue_channels();
        establish_connection_with_orchestration_queue();
        establish_orchestration_queue_channels();
        setup_orchestration_consumer();
    }

    private void establish_environment_variables() {
        if(containerized) {
            this.queue_host = "broker-queue";
            this.queue_port = 5672;
            this.producer_orchestration_queue_host = "orch-queue";
            this.producer_orchestration_queue_port = 5672;
            this.results_database_host = "results";
            this.results_database_port = 6379;
            this.redis_database_host = "redis-db";
            this.redis_database_port = 6379;
        } else {
            this.queue_host = "localhost";
            this.queue_port = 5675;
            this.producer_orchestration_queue_host = "localhost";
            this.producer_orchestration_queue_port = 5679;
            this.results_database_host = "localhost";
            this.results_database_port = 6380;
            this.redis_database_host = "localhost";
            this.redis_database_port = 6379;
        }
    }

    private void establish_connection_with_results_database() {
        log.info("🕋 Connecting to the \"RESULTS DATABASE\"...");
        this.results_database_pool = new JedisPool(this.results_database_host, this.results_database_port);
        log.info("✅ Successfuly connected to the \"RESULTS DATABASE\"!");
    }

    private void establish_connection_with_redis_database() {
        log.info("🕋 Connecting to the \"REDIS DATABASE\"...");
        this.redis_database_pool = new JedisPool(this.redis_database_host, this.redis_database_port);
        log.info("✅ Successfuly connected to the \"REDIS DATABASE\"!");
    }
    
    private void establish_connection_with_orchestration_queue() {
        log.info("🕋 Connecting to the \"ORCHESTRATION QUEUE\"...");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(producer_orchestration_queue_host);
        factory.setPort(producer_orchestration_queue_port);
        try {
            this.orchestration_queue_connection = factory.newConnection();
            log.info("✅ Successfuly connected to the \"ORCHESTRATION QUEUE\"!");
        } catch(IOException | TimeoutException e) {
            log.info("❌ Could not connect to the \"ORCHESTRATION QUEUE\"!");
        }
    }

    private void establish_orchestration_queue_channels() {
        try {
            this.orchestration_queue_orchestration_channel = this.orchestration_queue_connection.createChannel();
            this.orchestration_queue_orchestration_channel.queueDeclare("orchestration", false, false, false, null);
        } catch(IOException e) {
            log.info("❌ Something went wrong while declaring the \"orchestration\" channel on the \"ORCHESTRATION QUEUE\"!");
        }
    }

    private void establish_connection_with_broker_queue() {
        log.info("🕋 Connecting to the \"BROKER QUEUE\"...");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(queue_host);
        factory.setPort(queue_port); 
        while(this.broker_queue_connection == null) {
            try {
                this.broker_queue_connection = factory.newConnection();
                log.info("✅ Successfuly connected to the \"BROKER QUEUE\"!");
            } catch(IOException | TimeoutException e) {
                log.info("❌ Could not connect to the \"BROKER QUEUE\"!. Retrying....");
                try {
                    Thread.sleep(3000);
                } catch(InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private void establish_broker_queue_channels() {
        try {
            this.broker_queue_messaging_channel = this.broker_queue_connection.createChannel();
            this.broker_queue_messaging_channel.queueDeclare("message_queue", false, false, false, null);
        } catch(IOException e) {
            log.info("❌ Something went wrong while declaring the \"message_queue\" channel on the \"BROKER QUEUE\"!");
        }
        try {
            this.broker_queue_orchestrationg_channel = this.broker_queue_connection.createChannel();
            this.broker_queue_orchestrationg_channel.queueDeclare("orchestration", false, false, false, null);
        } catch(IOException e) {
            log.info("❌ Something went wrong while declaring the \"orchestration\" channel on the \"BROKER QUEUE\"!");
        }
    }

    private static Message message_generator(final int message_id, Random random, int olt_count, int type) {
        // TODO: Verificar se as constraints estao a ser cumprida
        long[] longer_than_timeout_times = {40000, 50000};
        long[] spikes_times = {10000, 20000, 30000};
        // type 0: random
        // type 1: processing time longer than timeout
        // type 2: spike
        int olt_identifier = random.nextInt(olt_count);
        long message_processing_time = 0;
        switch(type) {
            case 1:
                message_processing_time = longer_than_timeout_times[random.nextInt(2)];
                break;
            case 2:
                message_processing_time = spikes_times[random.nextInt(3)];
                break;
            default:
                // TODO: 500 < t < 4000
                message_processing_time = random.nextInt(4) * 1000 + 1000; // 1000 < t < 4000
                break;
        }
        String olt_name = String.valueOf(olt_identifier);
        Message m = new Message(message_id, olt_name, message_processing_time, message_timeout);
        return m;
    }

    private void generate_and_send_messages(Orchestration orchestration) {
        // 1% of the messages will take longer than timeout to process [30000, 40000, 50000]
        // 3% of the messages will take either [10000, 15000, 19000] to process

        int longer_than_timeout = (int) Math.floor(orchestration.get_messages() * 0.01);
        int spikes = (int) Math.floor(orchestration.get_messages() * 0.03);
        Random r = new Random(42);
        List<Integer> longer_than_timeouts_order_numbers = new ArrayList<>();
        for(int i = 0; i < longer_than_timeout; i++) {
            int order_number = r.nextInt(orchestration.get_messages());
            while(longer_than_timeouts_order_numbers.contains(order_number)) {
                order_number = r.nextInt(orchestration.get_messages());
            }
            longer_than_timeouts_order_numbers.add(order_number);
        }
        List<Integer> spikes_order_numbers = new ArrayList<>();
        for(int i = 0; i < spikes; i++) {
            int order_number = r.nextInt(orchestration.get_messages());
            while(longer_than_timeouts_order_numbers.contains(order_number) || spikes_order_numbers.contains(order_number)) {
                order_number = r.nextInt(orchestration.get_messages());
            }
            spikes_order_numbers.add(order_number);
        }
        for(int i = 0; i < orchestration.get_messages(); i++) {
            Message m;
            if(longer_than_timeouts_order_numbers.contains(i)) {
                m = message_generator(message_id++, r, orchestration.get_olts(), 1);
            } else if(spikes_order_numbers.contains(i)) {
                m = message_generator(message_id++, r, orchestration.get_olts(), 2);
            } else {
                m = message_generator(message_id++, r, orchestration.get_olts(), 0);
            }
            m.set_enqueued_at_broker(new Date().getTime());
            try {
                this.broker_queue_messaging_channel.basicPublish("", "message_queue", null, converter.toJson(m).getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                log.info("❌ Something went wrong while publishing a new message on the \"message_queue\" channel of the \"BROKER_QUEUE\"!");
            }
            log.info("📢 Published '" + converter.toJson(m) + "' to the broker.");
            int sleep_time = (r.nextInt(10) + 5) * 100;
            try {
                Thread.sleep(sleep_time);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Response> generate_run_results(Orchestration orchestration) {
        List<Response> responses = new ArrayList<>();
        try(Jedis jedis = this.results_database_pool.getResource()) {
            long key_count = jedis.dbSize();
            boolean not_all_request_reports_are_ready = (key_count < orchestration.get_messages());
            if(not_all_request_reports_are_ready) {
                log.info("🐌 Waiting for all request reports to be ready in order to calculate this runs' results...");
            }
            while(not_all_request_reports_are_ready) {
                Thread.sleep(2000);
                key_count = jedis.dbSize();
                not_all_request_reports_are_ready = (key_count < orchestration.get_messages());
            }
            Set<byte[]> result_keys = jedis.keys("*".getBytes(StandardCharsets.UTF_8));
            for(byte[] key : result_keys) {
                Response res_record = App.converter.fromJson(jedis.get(new String(key)), Response.class);
                responses.add(res_record);
            }
        } catch(Exception e) {
            log.info("❌ Something went wrong while getting resource from \"RESULTS\" database!");
        }
        return responses;
    }

    private static String metrics_calculator(Orchestration orchestration, List<Response> results) {
        List<RequestReport> reports = new ArrayList<>();
        for(Response r : results) {
            int request_id = r.get_origin_message().get_id();
            String olt = r.get_origin_message().get_olt();
            long total_time = r.get_origin_message().get_completed() - r.get_origin_message().get_issued_at();
            long time_broker_queue = r.get_origin_message().get_dequeued_at_broker() - r.get_origin_message().get_enqueued_at_broker();
            long time_worker_queue = r.get_origin_message().get_dequeued_at_worker() - r.get_origin_message().get_enqueued_at_worker();
            long time_olt_queue = r.get_origin_message().get_dequeued_at_olt() - r.get_origin_message().get_enqueued_at_olt();
            boolean timedout = r.get_timedout();
            RequestReport report = new RequestReport(request_id, olt, total_time, time_broker_queue, time_worker_queue, time_olt_queue, timedout);
            reports.add(report);
        }
        long total_time_total = 0;
        long time_broker_queue_total = 0;
        long time_worker_queue_total = 0;
        long time_olt_queue_total = 0;
        long timedout_requests = 0;
        for(RequestReport r : reports) {
            total_time_total += r.get_total_time();
            time_broker_queue_total += r.get_time_broker_queue();
            time_worker_queue_total += r.get_time_worker_queue();
            time_olt_queue_total += r.get_time_olt_queue();
            if(r.get_timedout() == true) timedout_requests = timedout_requests + (long) 1;
        }
        double avg_time_total = total_time_total / results.size();
        double avg_time_broker_queue = time_broker_queue_total / results.size();
        double avg_time_worker_queue = time_worker_queue_total / results.size();
        double avg_time_olt_queue = time_olt_queue_total / results.size();
        double percentage_of_timedout_requests = (double) timedout_requests / (double) results.size();
        if(orchestration.get_algorithm() == 3) {
            return "avg_time_total=" + avg_time_total + ", avg_time_broker_queue=" + avg_time_broker_queue + ", avg_time_olt_queue=" + avg_time_olt_queue + ", %timedout=" + percentage_of_timedout_requests;
        } else {
            return "avg_time_total=" + avg_time_total + " , avg_time_broker_queue=" + avg_time_broker_queue + ", avg_time_worker_queue=" + avg_time_worker_queue + ", avg_time_olt_queue=" + avg_time_olt_queue + ", %timedout=" + percentage_of_timedout_requests;
        }
    }

    public void wipe_redis_databases(JedisPool results_database_pool, JedisPool redis_database_pool) {
        try(Jedis jedis = results_database_pool.getResource()) {
            jedis.flushAll();
        }
        try(Jedis jedis = redis_database_pool.getResource()) {
            jedis.flushAll();
        }
        log.info("🧹✅ Both the results and redis database have been wiped");
    }

    private void setup_orchestration_consumer() {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            log.info("💬 Got a request a new run!");
            String jsonString = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Orchestration orchestration = converter.fromJson(jsonString, Orchestration.class);
            while(on_going_run == true) {
                try {
                    Thread.sleep(10000);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.info("🤩 About to start a new run!");
            forward_orchestration(orchestration);
            log.info("✅ Started a new run!");
            perform_run(orchestration);
        };
        try {
            this.orchestration_queue_orchestration_channel.basicConsume("orchestration", true, deliverCallback, consumerTag -> {});
        } catch(IOException e) {
            log.info("❌ Something went wrong while consuming message from \"orchestration\" on \"ORCHESTRATION QUEUE\"!");
        }
    }

    private void perform_run(Orchestration orchestration) {
        log.info("🏎  Starting a new run!");
        generate_and_send_messages(orchestration);
        List<Response> responses = generate_run_results(orchestration);
        String results_string = metrics_calculator(orchestration, responses);
        log.info("📖  " + results_string);
        wipe_redis_databases(results_database_pool, redis_database_pool);
        log.info("🏁 The run is finished!");
    }

    private void forward_orchestration(Orchestration orchestration) {
        try {
            this.broker_queue_orchestrationg_channel.basicPublish("", "orchestration", null, converter.toJson(orchestration).getBytes(StandardCharsets.UTF_8));
        } catch(IOException e) {
            log.info("❌ Something went wrong while forwarding orchestration to \"orchestration\" on the \"BROKER QUEUE\"!");
        }
        try {
            Thread.sleep(3000);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }



}
