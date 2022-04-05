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
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private int run_id = 0;
    private long start_instant;

    private String queue_host;
    private int queue_port;
    private String producer_orchestration_queue_host;
    private int producer_orchestration_queue_port;
    private String results_database_host;
    private int results_database_port;
    private String redis_database_host;
    private int redis_database_port;
    private String run_results_relational_database_host;
    private int run_results_relational_database_port;

    private JedisPool redis_database_pool;
    private JedisPool results_database_pool;
    private Connection broker_queue_connection;
    private Connection orchestration_queue_connection;
    private Channel orchestration_queue_orchestration_channel;
    private Channel broker_queue_messaging_channel;
    private Channel broker_queue_orchestrationg_channel;
    private java.sql.Connection run_results_database_connection;

    Logger log = LoggerFactory.getLogger(this.getClass().getName());

    private Map<Integer, Integer> processing_times_ocurrences = new HashMap<>();

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
        establish_connection_with_run_results_database();
        setup_orchestration_consumer();
    }

    private void establish_environment_variables() {
        if(containerized) {
            this.queue_host = "broker-queue";
            this.queue_port = 5672;
            this.producer_orchestration_queue_host = "orch-queue";
            this.producer_orchestration_queue_port = 5672;
            this.results_database_host = "results-db";
            this.results_database_port = 6379;
            this.redis_database_host = "redis-db";
            this.redis_database_port = 6379;
            this.run_results_relational_database_host = "run-results-db";
            this.run_results_relational_database_port = 5432;
        } else {
            this.queue_host = "localhost";
            this.queue_port = 5675;
            this.producer_orchestration_queue_host = "localhost";
            this.producer_orchestration_queue_port = 5679;
            this.results_database_host = "localhost";
            this.results_database_port = 6380;
            this.redis_database_host = "localhost";
            this.redis_database_port = 6379;
            this.run_results_relational_database_host = "localhost";
            this.run_results_relational_database_port = 5432;
        }
    }

    private void establish_connection_with_results_database() {
        log.info("STATUS: Connecting to the results database at: " + this.results_database_host + ":" + this.results_database_port);
        boolean success = false;
        while(success == false) {
            this.results_database_pool = new JedisPool(this.results_database_host, this.results_database_port);
            try {
                Jedis jedis = this.results_database_pool.getResource();
                success = true;
                jedis.close();
            } catch(JedisConnectionException e) {
                try {
                Thread.sleep(3000);
                } catch(InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        log.info("SUCCESS: Connected to the results database");
    }

    private void establish_connection_with_redis_database() {
        log.info("STATUS: Connecting to the redis database at: " + this.redis_database_host + ":" + this.redis_database_port);
        boolean success = false;
        while(success == false) {
            this.redis_database_pool = new JedisPool(this.redis_database_host, this.redis_database_port);
            try {
                Jedis jedis = this.redis_database_pool.getResource();
                success = true;
                jedis.close();
            } catch(JedisConnectionException e) {
                try {
                    Thread.sleep(3000);
                } catch(InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        log.info("SUCCESS: Connected to the redis database");
    }
   
    private void establish_connection_with_orchestration_queue() {
        log.info("STATUS: Connecting to the orchestration queue at: " + this.producer_orchestration_queue_host + ":" + this.producer_orchestration_queue_port);
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(this.producer_orchestration_queue_host);
        factory.setPort(this.producer_orchestration_queue_port);
        while(this.orchestration_queue_connection == null) {
            try {
                this.orchestration_queue_connection = factory.newConnection();
            } catch(IOException | TimeoutException e) {
                try {
                    Thread.sleep(3000);
                } catch(InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        log.info("SUCCESS: Connected to the orchestration queue");
    }

    private void establish_orchestration_queue_channels() {
        try {
            this.orchestration_queue_orchestration_channel = this.orchestration_queue_connection.createChannel();
            this.orchestration_queue_orchestration_channel.queueDeclare("orchestration", false, false, false, null);
        } catch(IOException e) {
            log.info("FAILURE: Something went wrong while declaring the \"orchestration\" channel on the orchestration queue");
        }
    }

    private void establish_connection_with_run_results_database() {
        while(this.run_results_database_connection == null) {
            try {
                log.info("STATUS: Connecting to the relation run results database at: " + this.run_results_relational_database_host + ":" + this.run_results_relational_database_port);
                Class.forName("org.postgresql.Driver");
                this.run_results_database_connection = DriverManager.getConnection("jdbc:postgresql://" + this.run_results_relational_database_host + ":" + this.run_results_relational_database_port + "/results", "postgres", "postgres");
            } catch(Exception e) {
                try {
                    Thread.sleep(3000);
                } catch(InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        log.info("SUCCESS: Connected to the run results database");
    }

    private void establish_connection_with_broker_queue() {
        log.info("STATUS: Connecting to the broker queue at: " + this.queue_host + ":" + this.queue_port);
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(this.queue_host);
        factory.setPort(this.queue_port); 
        while(this.broker_queue_connection == null) {
            try {
                this.broker_queue_connection = factory.newConnection();
            } catch(IOException | TimeoutException e) {
                try {
                    Thread.sleep(3000);
                } catch(InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        log.info("SUCCESS: Connected to the broker queue");
    }

    private void establish_broker_queue_channels() {
        try {
            this.broker_queue_messaging_channel = this.broker_queue_connection.createChannel();
            this.broker_queue_messaging_channel.queueDeclare("message_queue", false, false, false, null);
        } catch(IOException e) {
            log.info("FAILURE: Something went wrong while declaring the \"message_queue\" channel on the broker queue");
        }
        try {
            this.broker_queue_orchestrationg_channel = this.broker_queue_connection.createChannel();
            this.broker_queue_orchestrationg_channel.queueDeclare("orchestration", false, false, false, null);
        } catch(IOException e) {
            log.info("FAILURE: Something went wrong while declaring the \"orchestration\" channel on the broker queue");
        }
    }

    private static Message message_generator(final int message_id, Random random, int olt_count, int type) {
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
                message_processing_time = random.nextInt(4) * 1000 + 1000; // 1000 < t < 4000
                break;
        }
        String olt_name = String.valueOf(olt_identifier);
        Message m = new Message(message_id, olt_name, message_processing_time, message_timeout);
        return m;
    }

    private void generate_and_send_messages(Orchestration orchestration) {
        // TODO: fazer variar estas percentagens
        // 1% of the messages will take longer than timeout to process [30000, 40000, 50000]
        // 3% of the messages will take either [10000, 15000, 19000] to process
        int longer_than_timeout = (int) Math.floor(orchestration.get_messages() * 0.01);
        int spikes = (int) Math.floor(orchestration.get_messages() * 0.03);
        // TODO: user seeds diferentes: p.ex mais 2
        // Seed 1: 42
        // Seed 2: 7
        // Seed 3: 34
        int seed = 34;
        Random r = new Random(seed);
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
            if(this.processing_times_ocurrences.containsKey((int) m.get_processing_time())) {
                int curr_ocurrences = this.processing_times_ocurrences.get((int) m.get_processing_time());
                this.processing_times_ocurrences.put((int) m.get_processing_time(), curr_ocurrences + 1);
            } else {
                this.processing_times_ocurrences.put((int) m.get_processing_time(), 1);
            }
            try {
                this.broker_queue_messaging_channel.basicPublish("", "message_queue", null, converter.toJson(m).getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                log.info("FAILURE: Something went wrong while publishing a new message on the \"message_queue\" channel of the broker queue");
            }
            log.info("STATUS: Published '" + converter.toJson(m) + "' to the broker");
            int sleep_time = (r.nextInt(10) + 5) * 100;
            try {
                Thread.sleep(sleep_time);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info("Used seed: " + seed);
    }

    public List<Response> generate_run_results(Orchestration orchestration) {
        List<Response> responses = new ArrayList<>();
        try(Jedis jedis = this.results_database_pool.getResource()) {
            long key_count = jedis.dbSize();
            boolean not_all_request_reports_are_ready = (key_count < orchestration.get_messages());
            if(not_all_request_reports_are_ready) {
                log.info("STATUS: Waiting for the run to be fully completed in order to get all the results");
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
            log.info("FAILURE: Something went wrong while getting resource from results database");
        }
        return responses;
    }

    private String metrics_calculator(Orchestration orchestration, List<Response> results, long end_instant) {
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
        try {
            double avg_time_total_2 = (end_instant - this.start_instant) / orchestration.get_messages();
            Statement stmt = null;
            String sql = "UPDATE results SET ";
            sql += "avg_time_total = " + String.valueOf(avg_time_total) + ", ";
            sql += "avg_time_broker_queue = " + String.valueOf(avg_time_broker_queue) + ", ";
            sql += "avg_time_worker_queue = " + String.valueOf(avg_time_worker_queue) + ", ";
            sql += "avg_time_olt_queue = " + String.valueOf(avg_time_olt_queue) + ", ";
            sql += "avg_time_total_2 = " + String.valueOf(avg_time_total_2) + ", ";
            sql += "end_instant = " + String.valueOf(end_instant) + ", ";
            sql += "timedout = " + String.valueOf(percentage_of_timedout_requests) + ", "; 
            sql += "status = \'completed\' WHERE run = " + orchestration.get_id() + ";";
            stmt = this.run_results_database_connection.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch(SQLException e) {
            e.printStackTrace();
            log.info("FAILURE: An error has ocurred while updating the run results");
        }
        return "avg_time_total=" + avg_time_total + " , avg_time_broker_queue=" + avg_time_broker_queue + ", avg_time_worker_queue=" + avg_time_worker_queue + ", avg_time_olt_queue=" + avg_time_olt_queue + ", %timedout=" + percentage_of_timedout_requests + ", processing_time_ocurrences=" + App.converter.toJson(this.processing_times_ocurrences);  
    }

    public void wipe_redis_databases(JedisPool results_database_pool, JedisPool redis_database_pool) {
        try(Jedis jedis = results_database_pool.getResource()) {
            jedis.flushAll();
        }
        try(Jedis jedis = redis_database_pool.getResource()) {
            jedis.flushAll();
        }
        log.info("SUCCESS: Both the results and redis databases have been flushed");
    }

    private void setup_orchestration_consumer() {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            log.info("STATUS: Received a request to perform a new run");
            String jsonString = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Orchestration orchestration = converter.fromJson(jsonString, Orchestration.class);
            this.start_instant = new Date().getTime();
            try {
                Statement stmt = null;
                String sql = "INSERT INTO results (run, algorithm, start_instant, olts, workers, requests, status) " +
                    "VALUES (" + String.valueOf(this.run_id++) + 
                    ", " + String.valueOf(orchestration.get_algorithm()) +
                    ", " + String.valueOf(start_instant) + 
                    ", " + String.valueOf(orchestration.get_olts()) +
                    ", " + String.valueOf(orchestration.get_workers()) + 
                    ", " + String.valueOf(orchestration.get_messages()) +
                    ", " + "\'waiting_to_start\'" + ");";
                stmt = this.run_results_database_connection.createStatement();
                stmt.executeUpdate(sql);
                stmt.close();
            } catch(SQLException e) {
                e.printStackTrace();
                log.info("FAILURE: An error has ocurred while submitting the run information to the database");
            }
            while(this.on_going_run == true) {
                try {
                    Thread.sleep(10000);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                Statement stmt = null;
                String sql = "UPDATE results SET status = \'on_going\' where run = " + orchestration.get_id() + ";";
                stmt = this.run_results_database_connection.createStatement();
                stmt.executeUpdate(sql);
                stmt.close();
            } catch(SQLException e) {
                e.printStackTrace();
                log.info("FAILURE: An error has ocurred while updated the run status");
            }
            log.info("STATUS: Forwarding orchestration to the workers broker and workers");
            forward_orchestration(orchestration);
            perform_run(orchestration);
        };
        try {
            this.orchestration_queue_orchestration_channel.basicConsume("orchestration", true, deliverCallback, consumerTag -> {});
        } catch(IOException e) {
            log.info("FAILURE: Something went wrong while consuming message from \"orchestration\" on the orchestration queue");
        }
    }

    private void perform_run(Orchestration orchestration) {
        log.info("STATUS: Started a new run");
        this.on_going_run = true;
        generate_and_send_messages(orchestration);
        List<Response> responses = generate_run_results(orchestration);
        long end_instant = new Date().getTime();
        String results_string = metrics_calculator(orchestration, responses, end_instant);
        log.info("RESULTS: " + results_string);
        wipe_redis_databases(results_database_pool, redis_database_pool);
        this.on_going_run = false;
        log.info("COMPLETION: The run has been completed");
    }

    private void forward_orchestration(Orchestration orchestration) {
        try {
            this.broker_queue_orchestrationg_channel.basicPublish("", "orchestration", null, converter.toJson(orchestration).getBytes(StandardCharsets.UTF_8));
        } catch(IOException e) {
            log.info("FAILURE: Something went wrong while forwarding orchestration to \"orchestration\" on the broker queue");
        }
        try {
            Thread.sleep(5000);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

}
