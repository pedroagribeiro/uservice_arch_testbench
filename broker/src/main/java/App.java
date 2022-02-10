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

    @Parameter(names = { "-workers" }, description = "Adresses of the running workers")
    private static int workers;

    @Parameter(names = { "-algorithm" }, description = "Algorithm to implement")
    private static int algorithm;

    @Parameter(names = { "-containerized"}, description = "Indicates wether the setup is containerized or not")
    private static boolean containerized;

    private String consuming_queue_host;
    private int consuming_queue_port;
    private String redis_database_host;
    private int redis_database_port;

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
        application.establish_environment_variables();
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

    private void establish_environment_variables() {
        if(containerized) {
            this.consuming_queue_host = "broker_queue";
            this.consuming_queue_port = 5672;
            this.redis_database_host = "redis_db";
            this.redis_database_port = 6379;
        } else {
            this.consuming_queue_host = "localhost";
            this.consuming_queue_port = 5675;
            this.redis_database_host = "localhost";
            this.redis_database_port = 6379;
        }
    }

    private JedisPool establish_connection_with_redis_database() {
        return new JedisPool(redis_database_host, redis_database_port);
    }

    private Connection establish_connection_with_broker_queue() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(consuming_queue_host);
        factory.setPort(consuming_queue_port);
        Connection connection = factory.newConnection();
        return connection;
    }

    private List<Connection> establish_connection_with_workers_queues() throws IOException, TimeoutException {
        List<Connection> connections = new ArrayList<>();
        for(int i = 0; i < workers; i++) {
            ConnectionFactory factory = new ConnectionFactory();
            if(containerized) {
                factory.setHost("worker" + i + "_queue");
                factory.setPort(5672);
            } else {
                factory.setHost("localhost");
                factory.setPort(5672 + i);
            }
            Connection connection = factory.newConnection();
            connections.add(connection);
        }
        return connections;
    }

    private Channel declare_queue(Connection connection, final String queue_name) throws IOException {
        Channel channel = connection.createChannel();
        channel.queueDeclare(queue_name, false, false, false, null);
        return channel;
    }

    private List<Channel> declare_queues(List<Connection> connections, final String queue_name) throws IOException {
        List<Channel> channels = new ArrayList<>();
        for(Connection connection : connections) {
            Channel channel = connection.createChannel();
            channel.queueDeclare(queue_name, false, false, false, null);
            channels.add(channel);
        }
        return channels;
    }

    private void process_message_logic_one(Message m, JedisPool redis_database_pool, List<Channel> workers_queues_channels, String queue_name, AtomicInteger last_chosen_worker) throws IOException {
        m.set_dequeued_at_broker(new Date().getTime());
        try(Jedis jedis = redis_database_pool.getResource()) {
            if(jedis.exists(m.get_olt())) {
                int worker_to_forward = Integer.parseInt(jedis.get(m.get_olt()));
                m.set_enqueued_at_worker(new Date().getTime());
                workers_queues_channels.get(worker_to_forward).basicPublish("", queue_name, null, converter.toJson(m).getBytes(StandardCharsets.UTF_8));
                log.info("Forwarded '" + converter.toJson(m) + "' to worker " + worker_to_forward);
            } else {
                int worker_to_forward = (last_chosen_worker.get() + 1) % workers;
                last_chosen_worker.set(worker_to_forward);
                m.set_enqueued_at_worker(new Date().getTime()); 
                workers_queues_channels.get(worker_to_forward).basicPublish("", queue_name, null, converter.toJson(m).getBytes(StandardCharsets.UTF_8));
                log.info("Forwarded '" + converter.toJson(m) + "' to worker " + worker_to_forward);
            }
        }
    }

    private void process_manage_logic_two(Message m, JedisPool redis_database_pool, List<Channel> workers_queues_channels, String queue_name, MessageDigest digester) throws IOException {
        m.set_dequeued_at_broker(new Date().getTime());
        byte[] diggested_message = digester.digest(m.get_olt().getBytes(StandardCharsets.UTF_8));
        int worker_to_forward = ByteBuffer.wrap(diggested_message).getInt() % workers;
        if(worker_to_forward < 0 || worker_to_forward > 2) {
            worker_to_forward = new Random().nextInt(3);
        }
        m.set_enqueued_at_worker(new Date().getTime());
        workers_queues_channels.get(worker_to_forward).basicPublish("", queue_name, null, converter.toJson(m).getBytes(StandardCharsets.UTF_8));
        log.info("Forwarded '" + converter.toJson(m) + "' to worker " + worker_to_forward);
    }

    public void start_logic_1() throws IOException, TimeoutException {
        AtomicInteger last_chosen_worker = new AtomicInteger(workers - 1);
        JedisPool redis_database_pool = establish_connection_with_redis_database();
        Connection broker_queue_connection = establish_connection_with_broker_queue();
        List<Connection> workers_queues_connections = establish_connection_with_workers_queues();
        Channel broker_queue_channel = declare_queue(broker_queue_connection, "message_queue");
        List<Channel> workers_queues_channels = declare_queues(workers_queues_connections, "message_queue");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String jsonString = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Message m = converter.fromJson(jsonString, Message.class);
            process_message_logic_one(m, redis_database_pool, workers_queues_channels, "message_queue", last_chosen_worker);
        };
        broker_queue_channel.basicConsume("message_queue", true, deliverCallback, consumerTag -> {});
    }

    public void start_logic_2() throws NoSuchAlgorithmException, IOException, TimeoutException {
        JedisPool redis_database_pool = establish_connection_with_redis_database();
        Connection broker_queue_connection = establish_connection_with_broker_queue();
        List<Connection> workers_queues_connections = establish_connection_with_workers_queues();
        Channel broker_queue_channel = declare_queue(broker_queue_connection, "message_queue");
        List<Channel> workers_queues_channels = declare_queues(workers_queues_connections, "message_queue");
        MessageDigest digester = MessageDigest.getInstance("SHA-256");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String jsonString = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Message m = converter.fromJson(jsonString, Message.class);
            process_manage_logic_two(m, redis_database_pool, workers_queues_channels, "message_queue", digester);
        };
        broker_queue_channel.basicConsume("message_queue", true, deliverCallback, consumerTag -> {});
    }

    public void start_logic_3() {
        log.info("👀 I ain't doing anything. Just seeing what's up!");
    }
}
