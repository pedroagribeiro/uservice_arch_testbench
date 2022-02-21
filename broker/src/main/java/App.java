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

    @Parameter(names = { "-containerized" }, description = "Indicates wether the setup is containerized or not")
    private static boolean containerized;

    private static final int WORKER_CONTAINERS = 3;

    private String broker_queue_host;
    private int broker_queue_port;
    private String redis_database_host;
    private int redis_database_port;

    private Connection broker_queue_connection;
    private Channel broker_queue_message_channel;
    private Channel broker_queue_orchestration_channel;

    private JedisPool redis_database_pool;
    private MessageDigest digester;
    private AtomicInteger last_chosen_worker;

    private int current_logic = 1;
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
        application.run();
    }

    public void run() {
        establish_environment_variables();
        establish_connection_with_redis_database();
        establish_connection_with_broker_queue();
        establish_broker_queue_channels();
        establish_connection_with_workers();
        establish_workers_queues_channels();
        setup_orchestration_consumer();
        setup_broker_queue_message_consumption();
    }

    private void establish_environment_variables() {
        if(containerized) {
            this.broker_queue_host = "broker_queue";
            this.broker_queue_port = 5672;
            this.redis_database_host = "redis_db";
            this.redis_database_port = 6379;
        } else {
            this.broker_queue_host = "localhost";
            this.broker_queue_port = 5675;
            this.redis_database_host = "localhost";
            this.redis_database_port = 6379;
        }
        this.last_chosen_worker = new AtomicInteger(0);
        try {
            this.digester = MessageDigest.getInstance("SHA-256");
        } catch(NoSuchAlgorithmException e) {
            log.info("❌ The algorithm \"SHA-526\" does not exist for the MessageDigester!");
        }
    }


    private void establish_connection_with_redis_database() {
        log.info("🕋 Connecting to the \"REDIS DATABASE\"...");
        this.redis_database_pool = new JedisPool(redis_database_host, redis_database_port);
        log.info("✅ Successfuly connected to the \"REDIS DATABASE\"!");
    }

    private void establish_connection_with_broker_queue() {
        log.info("🕋 Connecting to the \"BROKER QUEUE\"...");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(this.broker_queue_host);
        factory.setPort(this.broker_queue_port);
        try {
            this.broker_queue_connection = factory.newConnection();
            log.info("✅ Successfuly connected to the \"BROKER QUEUE\"!");
        } catch(IOException | TimeoutException e) {
            e.printStackTrace();
            log.info("❌ Could not connect to the \"BROKER QUEUE\"!");
        }
    }

    private void establish_broker_queue_channels() {
        try {
            this.broker_queue_message_channel = this.broker_queue_connection.createChannel();
            this.broker_queue_message_channel.queueDeclare("message_queue", false, false, false, null);
        } catch(IOException e) {
            log.info("❌ An error ocurred while creating the \"message_queue\" channel on the \"BROKER QUEUE\"!");
        }
        try {
            this.broker_queue_orchestration_channel = this.broker_queue_connection.createChannel();
            this.broker_queue_orchestration_channel.queueDeclare("orchestration", false, false, false, null);
        } catch(IOException e) {
            log.info("❌ An error ocurred while creating the \"orchestration\" channel on the \"BROKER QUEUE\"!");
        }
    }

    public void establish_connection_with_workers() {
        log.info("🕋 Connecting to the \"WORKERS QUEUES\"...");
        List<Connection> connections = new ArrayList<>();
        for(int i = 0; i < App.WORKER_CONTAINERS; i++) {
            ConnectionFactory factory = new ConnectionFactory();
            if(containerized) {
                factory.setHost("worker" + i + "_queue");
                factory.setPort(5672);
            } else {
                factory.setHost("localhost");
                factory.setPort(5672 + i);
            }
            Connection connection = null;
            try {
                connection = factory.newConnection();
            } catch(IOException | TimeoutException e) {
                log.info("❌ Could not connect to \"WORKER " + i + " QUEUE\"!");
            }
            if(connection != null) {
                connections.add(connection);
            }
        }
        this.workers_queues_connections = connections;
        log.info("✅ Successfuly connected to the \"WORKERS QUEUES\"!");
    }

    private void establish_channels_with_workers_queues(Orchestration orchestration) {
        if(this.workers_queues_message_channels == null) {
            this.workers_queues_message_channels = new ArrayList<>();
        }
        for(int i = 0; i < this.workers_queues_message_channels.size(); i++) {
            try {
                this.workers_queues_message_channels.get(i).close();
            } catch(IOException | TimeoutException e) {
                log.info("❌ An error ocurred while closing the \"message_queue\" channel of \"WORKER " + i + "\"");
            }
        }
        this.workers_queues_message_channels.clear();
        for(int i = 0; i < orchestration.get_workers(); i++) {
            try {
                Channel worker_channel = this.workers_queues_connections.get(i).createChannel();
                worker_channel.queueDeclare("message_queue", false, false, false, null);
                this.workers_queues_message_channels.add(worker_channel);
            } catch(IOException e) {
                log.info("❌ An error ocurred while opening up the \"message_queue\" channel of \"WORKER " + i + "\"");
            }
        }
    }

    private void establish_workers_queues_channels() {
        if(this.workers_queues_orchestration_channels == null) {
            this.workers_queues_orchestration_channels = new ArrayList<>();
        }
        for(int i = 0; i < this.workers_queues_connections.size(); i++) {
            try {
                Channel channel = this.workers_queues_connections.get(i).createChannel();
                channel.queueDeclare("orchestration", false, false, false, null);
                this.workers_queues_orchestration_channels.add(channel);
            } catch(IOException e) {
                log.info("❌ An error ocurred while opening up the \"orchestration\" channel of \"WORKER " + i + "\"");
            }
        }
    }

    private void process_message_logic_one(Message m) {
        try(Jedis jedis = this.redis_database_pool.getResource()) {; 
            int worker_to_forward = 0;
            if(jedis.exists(m.get_olt())) {
                worker_to_forward = Integer.parseInt(jedis.get(m.get_olt()));
            } else {
                worker_to_forward = (last_chosen_worker.get() + 1) % this.workers_queues_message_channels.size();
                last_chosen_worker.set(worker_to_forward);
            }
            m.set_enqueued_at_worker(new Date().getTime());
            try {
                this.workers_queues_message_channels.get(worker_to_forward).basicPublish("", "message_queue", null, converter.toJson(m).getBytes(StandardCharsets.UTF_8));
            } catch(IOException e) {
                log.info("❌ Something has gone wrong while sending the message to \"WORKER " + worker_to_forward + "\"");
            }
            log.info("📤 Forwarded '" + converter.toJson(m) + "' to worker " + worker_to_forward);
        }
    }

    private void process_manage_logic_two(Message m) {
        byte[] diggested_message = this.digester.digest(m.get_olt().getBytes(StandardCharsets.UTF_8));
        int worker_to_forward = ByteBuffer.wrap(diggested_message).getInt() % this.workers_queues_message_channels.size();
        if(worker_to_forward < 0 || worker_to_forward > 2) {
            worker_to_forward = new Random().nextInt(3);
        }
        m.set_enqueued_at_worker(new Date().getTime());
        try {
            workers_queues_message_channels.get(worker_to_forward).basicPublish("", "message_queue", null, converter.toJson(m).getBytes(StandardCharsets.UTF_8));
        } catch(IOException e) {
            log.info("❌ Something has gone wrong while sending the message to \"WORKER " + worker_to_forward + "\"");
        }
        log.info("📤 Forwarded '" + converter.toJson(m) + "' to worker " + worker_to_forward);
    }

    public void forward_orchestration_to_workers(Orchestration orchestration) {
        for(int i = 0; i < this.workers_queues_orchestration_channels.size(); i++) {
            try {
                this.workers_queues_orchestration_channels.get(i).basicPublish("", "orchestration", null, converter.toJson(orchestration).getBytes(StandardCharsets.UTF_8));
            } catch(IOException e) {
                log.info("❌ Something has gone wrong while forwarding the orchestration to \"WORKER " + i + "\"");
            }
        }
        try {
            Thread.sleep(4000);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setup_orchestration_consumer() {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            log.info("🔀 Got a new orchestration request...");
            String jsonString = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Orchestration orchestration = converter.fromJson(jsonString, Orchestration.class); 
            current_logic = orchestration.get_algorithm();
            if(current_logic == 3) {
                try {
                    this.broker_queue_message_channel.close();
                } catch(TimeoutException e) {
                    log.info("❌ An error has ocurred closing the \"message_queue\" channel on the \"BROKER QUEUE\"!");
                }
            } else {
                establish_channels_with_workers_queues(orchestration);
                establish_broker_queue_channels();
                setup_broker_queue_message_consumption();
            }
            forward_orchestration_to_workers(orchestration);
            String algorithm = null;
            switch(current_logic) {
                case 1:
                    algorithm = "1️⃣";
                    break;
                case 2:
                    algorithm = "2️⃣";
                    break;
                case 3:
                    algorithm = "3️⃣";
                    break;
                default:
                    break;
            }
            log.info("✅ The new orchestration request imposed changes are now in effect! - Running algorithm " + algorithm);
            if(current_logic == 3) {
                log.info("👀 I'm only watching what's up. Not really handling anything...");
            }
        };
        try {
            this.broker_queue_orchestration_channel.basicConsume("orchestration", true, deliverCallback, consumerTag -> {});
        } catch(IOException e) {
            log.info("❌ Something went wrong when consuming messages from \"orchestration\" on the \"BROKER QUEUE\"!");
        }
    }

    public void setup_broker_queue_message_consumption() {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String jsonString = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Message m = converter.fromJson(jsonString, Message.class);
            m.set_dequeued_at_broker(new Date().getTime());
            switch(current_logic) {
                case 1:
                    process_message_logic_one(m);
                    break;
                case 2:
                    process_manage_logic_two(m);
                    break;
                default:
                    break;
            }
        };
        try {
            this.broker_queue_message_channel.basicConsume("message_queue", true, deliverCallback, consumerTag -> {});
        } catch(IOException e) {
            log.info("❌ Something went wrong when consuming messages from \"message_queue\" on the \"BROKER QUEUE\"");
        }
    }

}
