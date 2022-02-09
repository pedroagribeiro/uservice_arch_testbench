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
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class App {

    @Parameter(names = { "-workers", "--workers_adresses"}, description = "Adresses of the running workers")
    private static List<String> workers;

    @Parameter(names = { "-redis_host" }, description = "Host of the Redis database")
    private static String redis_host;

    @Parameter(names = { "-redis_port" }, description = "Port in which the database is exposed on the host")
    private static int redis_port;

    @Parameter(names = { "-worker_port" }, description = "Port exposed by the workers")
    private static int worker_port;

    @Parameter(names = { "-consuming_queue_host" }, description = "Consumer queue host")
    private static String queue_host;

    @Parameter(names = { "-consuming_queue_port"}, description = "Consumer queue port")
    private static int queue_port;

    @Parameter(names = { "-algorithm" }, description = "Algorithm to implement")
    private static int algorithm;

    private Gson converter = new Gson();

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    public static void main(String[] args) throws IOException, TimeoutException, NoSuchAlgorithmException {
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
        switch(algorithm) {
            case 1:
                application.start_logic_1();
                break;
            case 2:
                application.start_logic_2();
                break;
            case 3:
                application.start_logic_3();
            default:
                break;
        }
    }

    public void start_logic_1() throws IOException, TimeoutException {
        // Round-robin setup
        AtomicInteger last_chosen_worker = new AtomicInteger(workers.size() - 1);
        // Setup Redis connection
        JedisPool pool = new JedisPool(redis_host, redis_port);
        // Setup RabbitMQ connection to the queue to be consumed
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(queue_host);
        factory.setPort(queue_port);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // Setup all the connections to the workers queues
        List<Channel> producing_channels = new ArrayList<>();
        for(int i = 0; i < workers.size(); i++) {
            ConnectionFactory fac = new ConnectionFactory();
            fac.setHost(workers.get(i));
            fac.setPort(worker_port);
            Connection prod_connection = fac.newConnection();
            Channel prod_channel = prod_connection.createChannel();
            channel.queueDeclare("message_queue", false, false, false, null);
            producing_channels.add(prod_channel);
        }

        channel.queueDeclare("message_queue", false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String jsonString = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Message m = converter.fromJson(jsonString, Message.class);
            m.set_dequeued_at_broker(new Date().getTime());
            try (Jedis jedis = pool.getResource()) {
                if(jedis.exists(m.get_olt())) {
                    int worker = Integer.parseInt(jedis.get(m.get_olt()));
                    producing_channels.get(worker).basicPublish("", "message_queue", null, converter.toJson(m).getBytes(StandardCharsets.UTF_8));
                    m.set_enqueued_at_worker(new Date().getTime());
                    log.info("Forwarded '" + converter.toJson(m) + "' to worker no. " + worker);
                } else {
                    int worker = (last_chosen_worker.get() + 1) % workers.size();
                    last_chosen_worker.set(worker);
                    producing_channels.get(worker).basicPublish("", "message_queue", null, converter.toJson(m).getBytes(StandardCharsets.UTF_8));
                    m.set_enqueued_at_worker(new Date().getTime());
                    log.info("Forwarded '" + converter.toJson(m) + "' to worker no. " + worker);
                }
            }
        };
        channel.basicConsume("message_queue", true, deliverCallback, consumerTag -> {});
    }

    public void start_logic_2() throws NoSuchAlgorithmException, IOException, TimeoutException {
        // Create MessageDigester
        MessageDigest digester = MessageDigest.getInstance("SHA-256");
        // Setup RabbitMQ connection to the queue to be consumed
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(queue_host);
        factory.setPort(queue_port);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // Setup all the connections to the workers queues
        List<Channel> producing_channels = new ArrayList<>();
        for(int i = 0; i < workers.size(); i++) {
            ConnectionFactory fac = new ConnectionFactory();
            fac.setHost(workers.get(i));
            fac.setPort(worker_port);
            Connection prod_connection = fac.newConnection();
            Channel prod_channel = prod_connection.createChannel();
            channel.queueDeclare("message_queue", false, false, false, null);
            producing_channels.add(prod_channel);
        }

        channel.queueDeclare("message_queue", false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String jsonString = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Message m = converter.fromJson(jsonString, Message.class);
            byte[] diggested_message = digester.digest(m.get_olt().getBytes(StandardCharsets.UTF_8));
            int worker = ByteBuffer.wrap(diggested_message).getInt() % workers.size();
            if(worker < 0 || worker > 2) {
                worker = new Random().nextInt(3);
            }
            producing_channels.get(worker).basicPublish("", "message_queue", null, converter.toJson(m).getBytes(StandardCharsets.UTF_8));
            m.set_enqueued_at_worker(new Date().getTime());
            log.info("Forwared '" + converter.toJson(m) + "' to worker no. " + worker);
        };
        channel.basicConsume("message_queue", true, deliverCallback, consumerTag -> {});

    }

    public void start_logic_3() {
        log.info("👀 I ain't doing anything. Just seeing what's up!");
    }
}
