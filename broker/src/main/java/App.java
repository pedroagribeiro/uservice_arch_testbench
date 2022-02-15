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

    @Parameter(names = { "-containerized"}, description = "Indicates wether the setup is containerized or not")
    private static boolean containerized;

    private String consuming_queue_host;
    private int consuming_queue_port;
    private String redis_database_host;
    private int redis_database_port;

    private Connection broker_queue_connection;
    private Channel broker_queue_message_channel;
    private Channel broker_queue_orchestration_channel;

    private int current_logic = 1;
    private int workers = 1;
    private List<Connection> workers_queues_connections;
    private List<Channel> workers_queues_message_channels;
    private List<Channel> workers_queues_orchestration_channels;

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
        application.run();
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
        log.info("🕋 Connecting to the \"REDIS DATABASE\"...");
        JedisPool pool = new JedisPool(redis_database_host, redis_database_port);
        log.info("✅ Successfuly connected to the \"REDIS DATABASE\"!");
        return pool;
    }

    private void establish_connection_with_broker_queue() throws IOException, TimeoutException {
        log.info("🕋 Connecting to the \"BROKER QUEUE\"...");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(consuming_queue_host);
        factory.setPort(consuming_queue_port);
        Connection connection = factory.newConnection();
        this.broker_queue_connection = connection;
        log.info("✅ Successfuly connected to the \"BROKER QUEUE\"!");
    }

    private void establish_connection_with_workers_queues(Orchestration orchestration) throws IOException, TimeoutException {
        log.info("🕋 Connecting to the \"WORKERS QUEUES\"...");
        List<Connection> connections = new ArrayList<>();
        for(int i = 0; i < orchestration.get_workers(); i++) {
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
        this.workers_queues_connections = connections;
        log.info("✅ Successfuly connected to the \"WORKERS QUEUES\"!");
    }

    private Channel declare_queue(Connection connection, final String queue_name) throws IOException {
        Channel channel = connection.createChannel();
        channel.queueDeclare(queue_name, false, false, false, null);
        return channel;
    }

    private void declare_message_queues(final String queue_name) throws IOException {
        List<Channel> channels = new ArrayList<>();
        for(Connection connection : workers_queues_connections) {
            Channel channel = connection.createChannel();
            channel.queueDeclare(queue_name, false, false, false, null);
            channels.add(channel);
        }
        this.workers_queues_message_channels = channels;
    }

    public void declare_orchestration_queues(final String queue_name) throws IOException {
        List<Channel> channels = new ArrayList<>();
        for(Connection connection : workers_queues_connections) {
            Channel channel = connection.createChannel();
            channel.queueDeclare(queue_name, false, false, false, null);
            channels.add(channel);
        }
        this.workers_queues_orchestration_channels = channels;
    }

    private void process_message_logic_one(Message m, JedisPool redis_database_pool, AtomicInteger last_chosen_worker) throws IOException {
        try(Jedis jedis = redis_database_pool.getResource()) {; 
            int worker_to_forward = 0;
            if(jedis.exists(m.get_olt())) {
                worker_to_forward = Integer.parseInt(jedis.get(m.get_olt()));
            } else {
                worker_to_forward = (last_chosen_worker.get() + 1) % workers;
                last_chosen_worker.set(worker_to_forward);
            }
            m.set_enqueued_at_worker(new Date().getTime());
            workers_queues_message_channels.get(worker_to_forward).basicPublish("", "message_queue", null, converter.toJson(m).getBytes(StandardCharsets.UTF_8));
            log.info("Forwarded '" + converter.toJson(m) + "' to worker " + worker_to_forward);
        }
    }

    private void process_manage_logic_two(Message m, JedisPool redis_database_pool, MessageDigest digester) throws IOException {
        byte[] diggested_message = digester.digest(m.get_olt().getBytes(StandardCharsets.UTF_8));
        int worker_to_forward = ByteBuffer.wrap(diggested_message).getInt() % workers;
        if(worker_to_forward < 0 || worker_to_forward > 2) {
            worker_to_forward = new Random().nextInt(3);
        }
        m.set_enqueued_at_worker(new Date().getTime());
        workers_queues_message_channels.get(worker_to_forward).basicPublish("", "message_queue", null, converter.toJson(m).getBytes(StandardCharsets.UTF_8));
        log.info("Forwarded '" + converter.toJson(m) + "' to worker " + worker_to_forward);
    }

    public void forward_orchestration_to_workers(Orchestration orchestration) throws IOException {
        for(Channel channel : workers_queues_orchestration_channels) {
            channel.basicPublish("", "orchestration", null, converter.toJson(orchestration).getBytes(StandardCharsets.UTF_8));
        }
        try {
            Thread.sleep(4000);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setup_orchestration_consumer(Channel orchestration_queue_channel) throws IOException {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            log.info("🔀 Got a new orchestration request...");
            String jsonString = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Orchestration orchestration = converter.fromJson(jsonString, Orchestration.class); 
            current_logic = orchestration.get_algorithm();
            workers = orchestration.get_workers();
            if(current_logic != 3) {
                try {
                    establish_connection_with_workers_queues(orchestration);
                    declare_message_queues("message_queue");
                    declare_orchestration_queues("orchestration");
                    forward_orchestration_to_workers(orchestration);
                } catch(TimeoutException e) {
                    e.printStackTrace();
                }
            }
            log.info("✅ The new orchestration request imposed changes are now in effect! - Running algorithm " + current_logic);
        };
        orchestration_queue_channel.basicConsume("orchestration", true, deliverCallback, consumerTag -> {});
    }

    public void setup_broker_queue_message_consumption(JedisPool redis_database_pool, AtomicInteger last_chosen_worker, MessageDigest digester) throws IOException {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String jsonString = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Message m = converter.fromJson(jsonString, Message.class);
            m.set_dequeued_at_broker(new Date().getTime());
            switch(current_logic) {
                case 1:
                    process_message_logic_one(m, redis_database_pool, last_chosen_worker);
                    break;
                case 2:
                    process_manage_logic_two(m, redis_database_pool, digester);
                    break;
                default:
                    break;
            }
        };
        broker_queue_message_channel.basicConsume("message_queue", true, deliverCallback, consumerTag -> {});
    }

    public void run() throws IOException, TimeoutException, NoSuchAlgorithmException {
        // Algorithm 1 - Round Robin Variable
        AtomicInteger last_chosen_worker = new AtomicInteger(workers - 1);
        // Alogirthm 2 - Message Digester
        MessageDigest digester = MessageDigest.getInstance("SHA-256");
        // Common variables
        JedisPool redis_database_pool = establish_connection_with_redis_database();
        establish_connection_with_broker_queue();
        broker_queue_message_channel = declare_queue(broker_queue_connection, "message_queue");
        broker_queue_orchestration_channel = declare_queue(broker_queue_connection, "orchestration");
        setup_orchestration_consumer(broker_queue_orchestration_channel);
        setup_broker_queue_message_consumption(redis_database_pool, last_chosen_worker, digester);
    }
}
