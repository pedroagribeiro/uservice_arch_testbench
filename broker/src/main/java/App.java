import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Envelope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.math.BigInteger;

public class App {


    @Parameter(names = { "-containerized" }, description = "Indicates wether the setup is containerized or not")
    private static boolean containerized;

    // This should be set to how many worker containers there are in the deployment
    private static final int WORKER_CONTAINERS = 3;

    private static int current_logic = 1;

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

    private List<Connection> workers_queues_connections;
    private List<Channel> workers_queues_message_channels;
    private List<Channel> workers_queues_orchestration_channels;

    private Gson converter = new Gson();

    Logger log = LoggerFactory.getLogger(this.getClass().getName());

    private DeliverCallback orchestration_deliver_callback = (consumerTag, delivery) -> {
        log.info("STATUS: Got a new orchestration request");
        String jsonString = new String(delivery.getBody(), StandardCharsets.UTF_8);
        Orchestration orchestration = converter.fromJson(jsonString, Orchestration.class); 
        App.current_logic = orchestration.get_algorithm();
        if(App.current_logic == 3 || App.current_logic == 4) {
            try {
                try {
                    this.broker_queue_message_channel.basicCancel("broker");
                } catch(IOException e1) {
                    log.info("STATUS: Consumption channel was already closed");
                }
                if(this.broker_queue_message_channel != null && this.broker_queue_message_channel.isOpen()) {
                    this.broker_queue_message_channel.close();
                }
                if(this.broker_queue_orchestration_channel != null && this.broker_queue_orchestration_channel.isOpen()) {
                    this.broker_queue_orchestration_channel.close();
                }
                if(this.broker_queue_connection != null && this.broker_queue_connection.isOpen()) {
                    this.broker_queue_connection.close();
                }
                this.broker_queue_message_channel = null;
                this.broker_queue_orchestration_channel = null;
                this.broker_queue_connection = null;
                establish_connection_with_broker_queue();
                establish_broker_queue_channels();
                setup_orchestration_consumer();
            } catch(TimeoutException e) {
                e.printStackTrace();
            }
        } else {
            establish_channels_with_workers_queues(orchestration);
            establish_connection_with_broker_queue();
            establish_broker_queue_channels();
            setup_broker_queue_message_consumption();
        }
        forward_orchestration_to_workers(orchestration);
        log.info("STATUS: The orchestration changes have been applied - Running algorithm " + App.current_logic);
    };
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
            this.broker_queue_host = "broker-queue";
            this.broker_queue_port = 5672;
            this.redis_database_host = "redis-db";
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
            log.info("FAILURE: The algorithm SHA-526 does not exist for the MessageDigester");
        }
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

    private void establish_connection_with_broker_queue() {
        if(this.broker_queue_connection == null) {
            log.info("STATUS: Connecting to the broker queue at: " + this.broker_queue_host + ":" + this.broker_queue_port);
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(this.broker_queue_host);
            factory.setPort(this.broker_queue_port);
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
        }
        log.info("SUCCESS: Connected to the broker queue");
    }

    private void establish_broker_queue_channels() {
        if(this.broker_queue_message_channel == null) {
            try {
                this.broker_queue_message_channel = this.broker_queue_connection.createChannel();
                this.broker_queue_message_channel.queueDeclare("message_queue", false, false, false, null);
            } catch(IOException e) {
                log.info("FAILURE: An error ocurred while creating the \"message_queue\" channel on the broker queue");
            }
        }
        if(this.broker_queue_orchestration_channel == null) {
            try {
                this.broker_queue_orchestration_channel = this.broker_queue_connection.createChannel();
                this.broker_queue_orchestration_channel.queueDeclare("orchestration", false, false, false, null);
            } catch(IOException e) {
                log.info("FAILURE: An error ocurred while creating the \"orchestration\" channel on the broker queue");
            }
        }
    }

    public void establish_connection_with_workers() {
        log.info("STATUS: Connecting to the worker queues");
        List<Connection> connections = new ArrayList<>();
        for(int i = 0; i < App.WORKER_CONTAINERS; i++) {
            log.info("STATUS: Connecting to worker " + i);
            ConnectionFactory factory = new ConnectionFactory();
            if(containerized) {
                factory.setHost("worker-queue" + i);
                factory.setPort(5672);
            } else {
                factory.setHost("localhost");
                factory.setPort(5672 + i);
            }
            Connection connection = null;
            while(connection == null) {
                try {
                    connection = factory.newConnection();
                } catch(IOException | TimeoutException e) {
                    try {
                        Thread.sleep(3000);
                    } catch(InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            if(connection != null) {
                connections.add(connection);
            }
        }
        this.workers_queues_connections = connections;
        log.info("SUCCESS: Connected to the workers queues");
    }

    private void establish_channels_with_workers_queues(Orchestration orchestration) {
        if(this.workers_queues_message_channels == null) {
            this.workers_queues_message_channels = new ArrayList<>();
        }
        for(int i = 0; i < this.workers_queues_message_channels.size(); i++) {
            try {
                if(this.workers_queues_message_channels.get(i).isOpen()) {
                    this.workers_queues_message_channels.get(i).close();
                }
            } catch(IOException | TimeoutException e) {
                log.info("FAILURE: An error ocurred while closing the \"message_queue\" channel of worker " + i);
            }
        }
        this.workers_queues_message_channels.clear();
        for(int i = 0; i < orchestration.get_workers(); i++) {
            try {
                Channel worker_channel = this.workers_queues_connections.get(i).createChannel();
                worker_channel.queueDeclare("message_queue", false, false, false, null);
                this.workers_queues_message_channels.add(worker_channel);
            } catch(IOException e) {
                log.info("FAILURE: An error ocurred while opening up the \"message_queue\" channel of worker " + i);
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
                log.info("FAILURE: An error ocurred while opening up the \"orchestration\" channel of worker " + i);
            }
        }
    }

    private void process_message_logic_one(Message m) {
        try(Jedis jedis = this.redis_database_pool.getResource()) { 
            int worker_to_forward = 0;
            if(jedis.exists(m.get_olt())) {
                try {
                    worker_to_forward = Integer.parseInt(jedis.get(m.get_olt()));
                } catch(NumberFormatException e) {
                    log.info("FAILED: Could not parse worker to forward. Defaulted to worker 0");
                }
            } else {
                worker_to_forward = (last_chosen_worker.get() + 1) % this.workers_queues_message_channels.size();
                last_chosen_worker.set(worker_to_forward);
            }
            m.set_enqueued_at_worker(new Date().getTime());
            try {
                this.workers_queues_message_channels.get(worker_to_forward).basicPublish("", "message_queue", null, converter.toJson(m).getBytes(StandardCharsets.UTF_8));
            } catch(IOException e) {
                log.info("FAILURE: Something has gone wrong while sending the message to worker " + worker_to_forward);
            }
            log.info("STATUS: Forwarded '" + converter.toJson(m) + "' to worker " + worker_to_forward + " using logic 1");
        }
    }

    private void process_manage_logic_two(Message m) {
        byte[] diggested_message = this.digester.digest(m.get_olt().getBytes(StandardCharsets.UTF_8));
        // int worker_to_forward = ByteBuffer.wrap(diggested_message).getInt() % this.workers_queues_message_channels.size();
        int worker_to_forward = (int) new BigInteger(diggested_message).longValue();
        if(worker_to_forward < 0 || worker_to_forward > 2) {
            worker_to_forward = new Random().nextInt(3);
        }
        m.set_enqueued_at_worker(new Date().getTime());
        try {
            workers_queues_message_channels.get(worker_to_forward).basicPublish("", "message_queue", null, converter.toJson(m).getBytes(StandardCharsets.UTF_8));
        } catch(IOException e) {
            log.info("FAILURE: Something has gone wrong while sending the message to worker " + worker_to_forward);
        }
        log.info("STATUS: Forwarded '" + converter.toJson(m) + "' to worker " + worker_to_forward + " using logic 2");
    }

    public void forward_orchestration_to_workers(Orchestration orchestration) {
        for(int i = 0; i < this.workers_queues_orchestration_channels.size(); i++) {
            try {
                this.workers_queues_orchestration_channels.get(i).basicPublish("", "orchestration", null, converter.toJson(orchestration).getBytes(StandardCharsets.UTF_8));
            } catch(IOException e) {
                log.info("FAILURE: Something has gone wrong while forwarding the orchestration to worker " + i);
            }
        }
        try {
            Thread.sleep(6000);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setup_orchestration_consumer() {
        try {
            this.broker_queue_orchestration_channel.basicConsume("orchestration", true, orchestration_deliver_callback, consumerTag -> {});
        } catch(IOException e) {
            log.info("FAILURE: Something went wrong when consuming messages from \"orchestration\" on the broker queue");
        }
    }

    public void setup_broker_queue_message_consumption() {
        DefaultConsumer consumer = new DefaultConsumer(this.broker_queue_message_channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String jsonString = new String(body, StandardCharsets.UTF_8);
                Message m = converter.fromJson(jsonString, Message.class);
                m.set_dequeued_at_broker(new Date().getTime());
                switch(App.current_logic) {
                    case 1:
                        process_message_logic_one(m);
                        break;
                    case 2:
                        process_manage_logic_two(m);
                        break;
                    default:
                        log.info("STATUS: In case this appeared the broker is stealing messages in a logic in which it shouldn't");
                        break;
                }
            }
        };
        try {
            this.broker_queue_message_channel.basicConsume("message_queue", true, "broker", consumer);
        } catch(IOException e) {
            log.info("FAILURE: Something went wrong when consuming messages from \"message_queue\" on the broker queue");
        }
    }
}
