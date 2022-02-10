import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

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

    @Parameter(names = { "-olts" }, description = "Number of different OLT's from which the messages may appear")
    private static int olt_number;

    @Parameter(names = { "-messages" }, description = "Number of consecutive messages to be generated")
    private static int messages_to_generate;

    @Parameter(names = { "-no_broker" }, description = "Describes if the broker process is being used")
    private static boolean no_broker;

    @Parameter(names = { "-containerized" }, description = "Describes if the running environment if fully containerized")
    private static boolean containerized;

    private String queue_host;
    private int queue_port;

    private static Gson converter = new Gson();

    Logger log = LoggerFactory.getLogger(this.getClass().getName());

    private int message_id = 0;

    public static void main(String[] args) throws Exception {
        App application = new App();
        JCommander commands = JCommander.newBuilder().addObject(application).build();
        try {
            commands.parse(args);
        } catch (Exception e) {
            commands.usage();
            System.exit(-1);
        }
        application.establish_environment_variables();
        application.run();
    }

    private void establish_environment_variables() {
        if(containerized) {
            this.queue_host = "broker_queue";
            this.queue_port = 5672;
        } else {
            this.queue_host = "localhost";
            this.queue_port = 5675;
        }
    }

    private JedisPool establish_connection_with_results_database() {
        JedisPool pool = null;
        if(containerized) {
            pool = new JedisPool("results", 6379);
        } else {
            pool = new JedisPool("localhost", 6380);
        }
        return pool;
    }

    private Connection establish_connection_with_broker_queue() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(queue_host);
        factory.setPort(queue_port); 
        Connection connection = factory.newConnection();
        return connection;
    }

    private Channel declare_queue(Connection connection, final String queue_name) throws IOException {
        Channel channel = connection.createChannel();
        channel.queueDeclare(queue_name, false, false, false, null);
        return channel;
    }

    private static Message message_generator(final int message_id, Random random, int olt_count) {
        int olt_identifier = random.nextInt(olt_count);
        int message_processing_time = random.nextInt(4) * 1000 + 1000; // 1000 < t < 4000
        String olt_name = String.valueOf(olt_identifier);
        Message m = new Message(message_id, olt_name, message_processing_time);
        return m;
    }

    private void generate_and_send_messages(Channel channel, int messages_to_generate, String queue_name) throws IOException, InterruptedException {
        Random r = new Random(42);
        for(int i = 0; i < messages_to_generate; i++) {
            Message m =  message_generator(message_id++, r, olt_number);
            m.set_enqueued_at_broker(new Date().getTime());
            channel.basicPublish("", queue_name, null, converter.toJson(m).getBytes(StandardCharsets.UTF_8));
            log.info("Published '" + converter.toJson(m) + "' to the broker.");
            int sleep_time = (r.nextInt(10) + 5) * 100;
            Thread.sleep(sleep_time);
        }
    }

    public List<RequestReport> generate_run_results(JedisPool results_database_pool, long messages_to_generate) throws InterruptedException {
        List<RequestReport> reports = new ArrayList<>();
        try(Jedis jedis = results_database_pool.getResource()) {
            long key_count = jedis.dbSize();
            boolean not_all_request_reports_are_ready = (key_count < messages_to_generate);
            if(not_all_request_reports_are_ready) {
                log.info("Waiting for all request reports to be ready in order to calculate this runs' results...");
            }
            while(not_all_request_reports_are_ready) {
                Thread.sleep(2000);
                key_count = jedis.dbSize();
                not_all_request_reports_are_ready = (key_count < messages_to_generate);
            }
            Set<byte[]> result_keys = jedis.keys("*".getBytes(StandardCharsets.UTF_8));
            for(byte[] key : result_keys) {
                RequestReport report = converter.fromJson(jedis.get(new String(key)), RequestReport.class);
                reports.add(report);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return reports;
    }

    private static String metrics_calculator(List<RequestReport> results) {
        long total_time_total = 0;
        long time_broker_queue_total = 0;
        long time_worker_queue_total = 0;
        for(RequestReport r : results) {
            total_time_total += r.get_total_time();
            time_broker_queue_total += r.get_time_broker_queue();
            time_worker_queue_total += r.get_time_worker_queue();
        }
        double avg_time_total = total_time_total / results.size();
        double avg_time_broker_queue = time_broker_queue_total / results.size();
        double avg_time_worker_queue = time_worker_queue_total / results.size();
        if(no_broker) {
            return "avg_time_total=" + avg_time_total + ", avg_time_broker_queue=" + avg_time_broker_queue;
        } else {
            return "avg_time_total=" + avg_time_total + " , avg_time_broker_queue=" + avg_time_broker_queue + ", avg_time_worker_queue=" + avg_time_worker_queue;
        }
    }

    public void run() throws IOException, TimeoutException, InterruptedException {
        JedisPool results_database_pool = establish_connection_with_results_database();
        Connection broker_queue_connection = establish_connection_with_broker_queue();
        Channel broker_queue_channel = declare_queue(broker_queue_connection, "message_queue");
        generate_and_send_messages(broker_queue_channel, messages_to_generate, "message_queue");
        List<RequestReport> reports = generate_run_results(results_database_pool, messages_to_generate);
        String results_string = metrics_calculator(reports);
        log.info(results_string);
    }

}
