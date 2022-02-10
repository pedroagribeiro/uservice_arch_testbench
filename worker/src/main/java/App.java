import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import com.rabbitmq.client.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeoutException;

public class App {

    @Parameter(names = { "-id" }, description = "The identifier of the current worker")
    private static int worker_id;

    @Parameter(names = { "-redis_host"}, description = "Host of the redis database")
    private static String redis_host;

    @Parameter(names = { "-redis_port" }, description = "Port in which the database is exposed on the redis database host")
    private static int redis_port;

    @Parameter(names = { "-poler"}, description = "Let us define wether the worker gets the message directly from the producers queue")
    private static int poler; 

    @Parameter(names = { "-producer_queue_host"}, description = "Host of the producers queue")
    private static String producer_queue_host;

    @Parameter(names = { "-producer_queue_port" }, description = "Port of the producers queue")
    private static int producer_queue_port;

    private static Gson converter = new Gson();

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    public static void main(String[] args) throws IOException, TimeoutException {
        App application = new App();
        JCommander commands = JCommander.newBuilder().addObject(application).build();
        try {
            commands.parse(args);
        } catch (Exception e) {
            commands.usage();
            System.exit(-1);
        }
        try {
        Thread.sleep(10000);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        if(poler == 1) {
            application.start_poler_logic();
        } else {
            application.start_not_poler_logic();

        }
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

    /**
     * A `worker` needs to establish 2 conceptual connections: 
     * 
     * 1. Needs a connection to the redis database that stores which worker is processing each OLT's request. 
     * 2. Needs a connection to it's own incoming queue to read the incoming requests. 
     * 3. Needs a connection to the OLT to which it has to forward the request.
     *    - one connection per OLT
     */

    public void start_not_poler_logic() throws IOException, TimeoutException {
        // Establish Redis connection - Quem esta a tratar o que
        JedisPool pool = new JedisPool(redis_host, redis_port);
        // Establish Redis connection - Results
        JedisPool results_pool = new JedisPool("localhost", 6380);
        Jedis results_jedis = results_pool.getResource();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672 + worker_id);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("message_queue", false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String jsonString = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Message m = converter.fromJson(jsonString, Message.class);
            m.set_dequeued_at_worker(new Date().getTime());
            log.info("Received: '" + converter.toJson(m) + "'");
            // Initialize connection with the corresponding OLT
            ConnectionFactory olt_factory = new ConnectionFactory();
            olt_factory.setHost("localhost");
            log.info("OLT: " + Integer.parseInt(m.get_olt()));
            olt_factory.setPort(5676 + Integer.parseInt(m.get_olt())); 
            try {
                Connection olt_connection = olt_factory.newConnection();
                Channel olt_channel = olt_connection.createChannel();
                olt_channel.queueDeclare("requests", false, false, false, null);
                olt_channel.basicPublish("", "requests", null, converter.toJson(m).getBytes(StandardCharsets.UTF_8));
                // wait at most 'Timeout' time for the response
                olt_channel.close();
                olt_connection.close();
            } catch(TimeoutException e) {
                e.printStackTrace();
            }
            try(Jedis jedis = pool.getResource()) {
                jedis.set(m.get_olt(), String.valueOf(worker_id));
                Thread.sleep(m.get_processing_time());
                m.set_completed(new Date().getTime());
                jedis.del(m.get_olt());
            } catch (Exception e) {
                e.printStackTrace();
            }
            RequestReport report = new RequestReport(m.get_id(), m.get_olt(), m.get_completed() - m.get_issued_at(), m.get_dequeued_at_broker() - m.get_enqueued_at_broker(), m.get_dequeued_at_worker() - m.get_enqueued_at_worker());
            results_jedis.set(String.valueOf(report.request_id), converter.toJson(report));
        };

        channel.basicConsume("message_queue", true, deliverCallback, consumerTag -> {});
    }

    public void start_poler_logic() throws IOException, TimeoutException {
        // Redis results database
        JedisPool results_pool = new JedisPool("localhost", 6380);
        Jedis results_jedis = results_pool.getResource();
        // Setup redis connection
        JedisPool pool = new JedisPool(redis_host, redis_port);
        // Conncet to the producer's queue
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(producer_queue_host);
        factory.setPort(producer_queue_port);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("message_queue", false, false, false, null);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String jsonString = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Message m = converter.fromJson(jsonString, Message.class);
            log.info(converter.toJson(m));
            try (Jedis jedis = pool.getResource()) {
                if(jedis.exists(m.get_olt())) {
                    boolean r = true;
                    while(r == true) {
                        try {
                            Thread.sleep(1000);
                            log.info("Waited 1 second.");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        r = jedis.exists(m.get_olt());
                    }
                }
                jedis.set(m.get_olt(), String.valueOf(worker_id));
                log.info("Received '" + converter.toJson(m) + "'");
                try {
                    Thread.sleep(m.get_processing_time());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                m.set_completed(new Date().getTime());
                jedis.del(m.get_olt());
                RequestReport report = new RequestReport(m.get_id(), m.get_olt(), m.get_completed() - m.get_issued_at(), m.get_dequeued_at_broker() - m.get_enqueued_at_broker(), m.get_dequeued_at_worker() - m.get_enqueued_at_worker()); 
                results_jedis.set(String.valueOf(report.request_id), converter.toJson(report));
            }
        };

        channel.basicConsume("message_queue", true, deliverCallback, consumerTag -> {});

    }
}
