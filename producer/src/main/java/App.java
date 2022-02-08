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
import java.net.Inet4Address;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeoutException;

public class App {

    @Parameter(names = { "-olts" , "--olt-count" }, description = "Number of different OLT's from which the messages may appear")
    private static int olt_number;

    @Parameter(names = { "-messages", "--message-count"}, description = "Number of consecutive messages to be generated")
    private static int messages_to_generate;

    @Parameter(names = { "-queue", "--queue-name" }, description = "Name of the queue to which the messages should be produced")
    private static String queue_name;

    @Parameter(names = { "-queue_host" }, description = "Where the queue is hosted")
    private static String queue_host;

    @Parameter(names = { "-queue_port"}, description = "The port in which the queue is exposed in the host")
    private static int queue_port;

    @Parameter(names = { "-directly_from_broker_queue"}, description = "When active the worker consume the broker queue directly")
    private static int directly_from_broker;

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
        InetAddress address = InetAddress.getByName(queue_host);
        System.out.println(address.getAddress());
        boolean reachable = address.isReachable(10000);
        System.out.println("Is network reachable: " + reachable);
        application.start();

    }
    private class RequestReport {

        private long request_id;
        private String olt;
        private long total_time;
        private long time_broker_queue;
        private long time_worker_queue;

        private RequestReport(final long request_id, final String olt, final long total_time, final long time_broker_queue, final long time_worker_queue) {
            this.request_id = request_id;
            this.olt = olt;
            this.total_time = total_time;
            this.time_broker_queue = time_broker_queue;
            this.time_worker_queue = time_worker_queue;
        }
        
        public long get_request_id() { 
            return this.request_id;
        }

        public void set_request_id(final long request_id) {
            this.request_id = request_id;
        }

        public String get_olt() {
            return this.olt;
        }

        public void set_olt(final String olt) {
            this.olt = olt;
        }

        public long get_total_time() {
            return this.total_time;
        }

        public void set_total_time(final long total_time) {
            this.total_time = total_time;
        }

        public long get_time_broker_queue() {
            return this.time_broker_queue;
        }

        public void set_time_broker_queue(final long time_broker_queue) {
            this.time_broker_queue = time_broker_queue;
        }

        public long get_time_worker_queue() {
            return this.time_worker_queue;
        }

        public void set_time_worker_queue(final long time_worker_queue) {
            this.time_worker_queue = time_worker_queue;
        }

        @Override
        public String toString() {
            return "RequestReport{" +
                    "request_id='" + request_id + '\'' +
                    ", olt='" + olt + '\'' +
                    ", total_time=" + total_time +
                    ", time_broker_queue=" + time_broker_queue +
                    ", time_worker_queue=" + time_worker_queue +
                    '}';
        }
    }

    public void start() throws IOException, TimeoutException, InterruptedException {
        // Redis database of the results connections
        JedisPool pool = new JedisPool("localhost", 6380);
        // JSON Converter
        Gson converter = new Gson();
        // Create a connection to the RabbitMQ server
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(queue_host);
        factory.setPort(queue_port);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(queue_name, false, false, false, null);
            for(int i = 0; i < messages_to_generate; i++) {
                Random rand = new Random();
                Message m = message_generator(message_id++);
                // Sleep Time: 500 < t < 5000
                int sleep_time = (rand.nextInt(10) + 5) * 100;
                m.set_enqueued_at_broker(new Date().getTime());
                channel.basicPublish("", queue_name, null, converter.toJson(m).getBytes(StandardCharsets.UTF_8));
                log.info("Published '" + converter.toJson(m) + "' to the broker");
                Thread.sleep(sleep_time);
            }
        }
        try(Jedis jedis = pool.getResource()) {
            List<RequestReport> reports = new ArrayList<>();
            long key_count = jedis.dbSize();
            // Enquanto não existirem 100 resultados espera
            while(key_count < messages_to_generate) {
                log.info("I got " + key_count + " results already.");
                Thread.sleep(2000);
                key_count = jedis.dbSize();
            }
            Set<byte[]> keys = jedis.keys("*".getBytes(StandardCharsets.UTF_8));
            for(byte[] key : keys) {
                RequestReport report = converter.fromJson(jedis.get(new String(key)), RequestReport.class);
                reports.add(report);
            }
            System.out.println(metrics_calculator(reports, directly_from_broker));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Message message_generator(final int message_id) {
        Random random = new Random();
        int olt_identifier = random.nextInt(olt_number + 1);
        int message_processing_time = random.nextInt(4) * 1000 + 1000; // 1000 < t < 4000
        String olt_name = "OLT" + olt_identifier;
        Message m = new Message(message_id, olt_name, message_processing_time);
        return m;
    }

    private static String metrics_calculator(List<RequestReport> results, int directly_from_broker) {
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
        if(directly_from_broker == 1) {
            return "avg_time_total=" + avg_time_total + ", avg_time_broker_queue=" + avg_time_broker_queue;
        } else {
            return "avg_time_total=" + avg_time_total + " , avg_time_broker_queue=" + avg_time_broker_queue + ", avg_time_worker_queue=" + avg_time_worker_queue;
        }
    }
}
