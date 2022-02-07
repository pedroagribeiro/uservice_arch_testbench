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

    @Parameter(names = { "-queue", "--queue_name" }, description = "The identifier queue from which the program is going to consume")
    private static String queue_name;

    @Parameter(names = { "-id", "--worker-id" }, description = "The identifier of the current worker")
    private static int worker_id;

    @Parameter(names = { "-redis_host"}, description = "Host of the redis database")
    private static String redis_host;

    @Parameter(names = { "-redis_port" }, description = "Port in which the database is exposed on the redis database host")
    private static int redis_port;

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

    public void start() throws IOException, TimeoutException {
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
        channel.queueDeclare(queue_name, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String jsonString = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Message m = converter.fromJson(jsonString, Message.class);
            m.set_handled_by_worker(new Date().getTime());
            log.info("Received: '" + converter.toJson(m) + "'");
            try(Jedis jedis = pool.getResource()) {
                jedis.set(m.get_olt(), String.valueOf(worker_id));
                Thread.sleep(m.get_processing_time());
                jedis.del(m.get_olt());
            } catch (Exception e) {
                e.printStackTrace();
            }
            RequestReport report = new RequestReport(m.get_id(), m.get_olt(), m.get_handled_by_worker() - m.get_issued_at(), m.get_forwarded_by_broker() - m.get_issued_at(), m.get_handled_by_worker() - m.get_forwarded_by_broker());
            results_jedis.set(String.valueOf(report.request_id), converter.toJson(report));
        };

        channel.basicConsume(queue_name, true, deliverCallback, consumerTag -> {});
    }
}
