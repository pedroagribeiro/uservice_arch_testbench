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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.awaitility.Awaitility.await;

public class App {

    @Parameter(names = { "-id" }, description = "The identifier of the current worker")
    private static int worker_id;

    @Parameter(names = { "-containerized" }, description = "Describes if the running environment is fully containerized")  
    private static boolean containerized;

    private static final int OLT_CONTAINERS = 3;

    private String worker_queue_host;
    private int worker_queue_port;
    private String broker_queue_host;
    private int broker_queue_port;
    private String redis_database_host;
    private int redis_database_port;
    private String redis_results_database_host;
    private int redis_results_database_port;

    private final static int WORKER_QUEUE = 0;
    private final static int BROKER_QUEUE = 1;

    private int current_consumption = WORKER_QUEUE;
    private Connection worker_queue_connection;
    private Connection broker_queue_connection;
    private Channel worker_queue_orchestration_channel; 
    private Channel worker_queue_olt_response_channel;
    private Channel current_consumption_channel;

    private JedisPool redis_database_pool;
    private JedisPool results_database_pool;

    private List<Connection> olts_connections = new ArrayList<>();
    private List<Channel> current_olts_request_channels;

    private static Gson converter = new Gson();

    private AtomicInteger current_active_request = new AtomicInteger();
    private final Map<Integer, Boolean> current_request_satisfied = new ConcurrentHashMap<>();

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
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
        establish_connection_with_redis_results_database();
        establish_connection_with_worker_queue();
        establish_worker_queue_channels();
        establish_connection_with_broker_queue();
        establish_current_consuming_channel();
        establish_connection_with_olts_queues();
        establish_olts_queues_channels(OLT_CONTAINERS);
        setup_orchestration_consumption();
        setup_message_consumption();
    } 

    private void establish_environment_variables() {
        if(containerized) {
            this.worker_queue_host = "worker-queue" + worker_id;
            this.worker_queue_port = 5672;
            this.broker_queue_host = "broker-queue";
            this.broker_queue_port = 5672;
            this.redis_database_host = "redis-db";
            this.redis_database_port = 6379;
            this.redis_results_database_host = "results";
            this.redis_results_database_port = 6379;
        } else {
            this.worker_queue_host = "localhost";
            this.worker_queue_port = 5672 + worker_id;
            this.broker_queue_host = "localhost";
            this.broker_queue_port = 5675;
            this.redis_database_host = "localhost";
            this.redis_database_port = 6379;
            this.redis_results_database_host = "localhost";
            this.redis_results_database_port = 6380;
        }
    }

    public void establish_connection_with_redis_database() {
        log.info("🕋 Connecting to the \"REDIS DATABASE\"...");
        this.redis_database_pool = new JedisPool(redis_database_host, redis_database_port);
        log.info("✅ Successfuly connected to the \"REDIS DATABASE\"!");
    }

    public void establish_connection_with_redis_results_database() {
        log.info("🕋 Connecting to the \"RESULTS DATABASE\"...");
        this.results_database_pool = new JedisPool(redis_results_database_host, redis_results_database_port);
        log.info("✅ Successfuly connected to the \"RESULTS DATABASE\"!");
    }

    public void establish_connection_with_worker_queue() {
        log.info("🕋 Connecting to the \"WORKER QUEUE\"...");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(worker_queue_host);
        factory.setPort(worker_queue_port);
        while(this.worker_queue_connection == null) {
        try {
                this.worker_queue_connection = factory.newConnection();
                log.info("✅ Successfuly connected to the \"WORKER QUEUE\"!");
            } catch(IOException | TimeoutException e) {
                log.info("❌ Could not connect to the \"WORKER QUEUE\"!. Retrying...");
                try {
                    Thread.sleep(3000);
                } catch(InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void establish_connection_with_broker_queue() {
        log.info("🕋 Connecting to the \"BROKER QUEUE\"...");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(broker_queue_host);
        factory.setPort(broker_queue_port);
        while(this.broker_queue_connection == null) {
            try {
                this.broker_queue_connection = factory.newConnection();
                log.info("✅ Successfuly connected to the \"BROKER QUEUE\"!");
            } catch(IOException | TimeoutException e) {
                log.info("❌ Could not connect to the \"BROKER QUEUE\"!. Retrying...");
                try {
                    Thread.sleep(3000);
                } catch(InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void establish_worker_queue_channels() {
        try {
            this.worker_queue_orchestration_channel = this.worker_queue_connection.createChannel();
            this.worker_queue_orchestration_channel.queueDeclare("orchestration", false, false, false, null);
        } catch(IOException e) {
            log.info("❌ Something went wrong while declaring the \"orchestration\" channel on the \"WORKER QUEUE\"!");
        }
        try {
            this.worker_queue_olt_response_channel = this.worker_queue_connection.createChannel();
            this.worker_queue_olt_response_channel.queueDeclare("responses", false, false, false, null);
        } catch(IOException e) {
            log.info("❌ Something went wrong while declaring the \"responses\" channel on the \"WORKER QUEUE\"!");
        }
    }

    public void establish_connection_with_olts_queues() {
        log.info("🕋 Connecting to the \"OLT QUEUE\"s...");
        for(int i = 0; i < App.OLT_CONTAINERS; i++) {
            ConnectionFactory factory = new ConnectionFactory();
            if(containerized) {
                factory.setHost("olt" + i + "_queue");
                factory.setPort(5672);
            } else {
                factory.setHost("localhost");
                factory.setPort(5676 + i);
            }
            Connection connection = null;
            while(connection == null) {
                try {
                    connection = factory.newConnection();
                    this.olts_connections.add(connection);
                } catch(IOException | TimeoutException e) {
                    log.info("❌ Something went wrong while connecting to \"OLT " + i + "\"!. Retrying...");
                    try {
                        Thread.sleep(3000);
                    } catch(InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        log.info("✅ Successfuly connected to the \"OLT QUEUE\"s!");
    }

    public void establish_olts_queues_channels(int olts) {
        log.info("🕋 Creating channels for communication with OLT's!");
        if(this.current_olts_request_channels != null) {
            for(int i = 0; i < this.current_olts_request_channels.size(); i++) {
                try {
                    this.current_olts_request_channels.get(i).close();
                } catch(IOException | TimeoutException e) {
                    log.info("❌ An error ocurred while closing the \"requests\" channel on the \"OLT " + i + " QUEUE\"!");
                }
            }
            this.current_olts_request_channels.clear();
        }
        if(this.current_olts_request_channels == null) {
            this.current_olts_request_channels = new ArrayList<>();
        }
        for(int i = 0; i < olts; i++) {
            try {
                Channel request_channel = this.olts_connections.get(i).createChannel();
                request_channel.queueDeclare("requests", false, false, false, null);
                this.current_olts_request_channels.add(request_channel);
            } catch(IOException e) {
                log.info("❌ An error ocurred while creating the \"requests\" channel on the \"OLT " + i + "QUEUE\"!");
            }
        }
        log.info("✅ Successfuly created channels for communication with OLT's!");
    }

    public void establish_current_consuming_channel() {
        if(this.current_consumption_channel != null) {
            try {
                this.current_consumption_channel.close();
            } catch(IOException | TimeoutException e) {
                log.info("❌ Something has gone wrong while trying to close the \"message_queue\" channel on the \"CONSUMING QUEUE\"!");
            }
        }
        try {
            if(current_consumption == WORKER_QUEUE) {
                this.current_consumption_channel = this.worker_queue_connection.createChannel();
            } else {
                this.current_consumption_channel = this.broker_queue_connection.createChannel();
            }
            this.current_consumption_channel.queueDeclare("message_queue", false, false, false, null);
        } catch(IOException e) {
            log.info("❌ Something has gone wrong while trying to declare the \"message_queue\" on the \"CONSUMING QUEUE\"!");
        } 
    }

    public void setup_message_consumption() {
        DeliverCallback deliverCallback = (consumerTag, deliver) -> {
            String jsonString = new String(deliver.getBody(), StandardCharsets.UTF_8);
            Message m = App.converter.fromJson(jsonString, Message.class);
            m.set_dequeued_at_worker(new Date().getTime());
            m.set_worker(worker_id);
            process_message(m);
        };
        try {
            this.current_consumption_channel.basicConsume("message_queue", true, deliverCallback, consumerTag -> {});
        } catch(IOException e) {
            log.info("❌ Something has gone wrong when trying to consume from \"message_queue\" channel in the \"CONSUMING QUEUE\"!");
        }
    }

    private void setup_orchestration_consumption() {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            log.info("🔀 Got new orchestration request...");
            String jsonString = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Orchestration orchestration = converter.fromJson(jsonString, Orchestration.class);
            if(orchestration.get_algorithm() == 3) {
                if(current_consumption == WORKER_QUEUE) {
                    current_consumption = BROKER_QUEUE;
                    establish_current_consuming_channel();
                } 
            } else {
                if(current_consumption == BROKER_QUEUE) {
                    current_consumption = WORKER_QUEUE;
                    establish_current_consuming_channel();
                }
            }
            establish_olts_queues_channels(orchestration.get_olts());
            setup_message_consumption();
            start_olts_responses_consuming_logic();
            log.info("✅ The new orchestration request imposed changes are now in effect!"); 
        };
        try {
            this.worker_queue_orchestration_channel.basicConsume("orchestration", true, deliverCallback, consumerTag -> {});
        } catch(IOException e) {
            log.info("❌ Something has gone wrong when trying to consume from \"orchestration\" channel in the \"WORKER QUEUE\"!");
        }
    }

    public Connection establish_connection_with_consuming_queue(Orchestration orchestration) {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = null;
        if(orchestration.get_algorithm() == 3) {
            log.info("🕋 Connecting to the \"BROKER QUEUE\"...");
            factory.setHost(broker_queue_host);
            factory.setPort(broker_queue_port);
            try {
                connection = factory.newConnection();
                log.info("✅ Successfuly connected to the \"BROKER QUEUE\"!");
            } catch(IOException | TimeoutException e) {
                log.info("❌ Could not connect to the \"BROKER QUEUE\"!");
            }
        } else {
            log.info("🕋 Connectiong to the \"WORKER_QUEUE\"...");
            factory.setHost(worker_queue_host);
            factory.setPort(worker_queue_port);
            try {
                connection = factory.newConnection();
                log.info("✅ Successfuly connected to the \"WORKER QUEUE\"!");
            } catch(IOException | TimeoutException e) {
                log.info("❌ Could not connect to the \"WORKER QUEUE\"!");
            }
        } 
        return connection;
    }

    private Callable<Boolean> request_satisfied(int request_id) {
        return () -> this.current_request_satisfied.get(request_id);
    }

    public void process_message(Message m) throws IOException {
        int target_olt = Integer.parseInt(m.get_olt());
        log.info("📥 Received: '" + converter.toJson(m) + "'");
        if(current_consumption == BROKER_QUEUE) {
            try(Jedis jedis = this.redis_database_pool.getResource()) {
                boolean handling_request_for_same_olt = jedis.exists(m.get_olt());
                while(handling_request_for_same_olt) {
                    try {
                        Thread.sleep(100);
                        log.info("cenas");
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                    handling_request_for_same_olt = jedis.exists(m.get_olt());
                }
                jedis.set(m.get_olt(), String.valueOf(worker_id));
            }
        } else {
            try(Jedis jedis = this.redis_database_pool.getResource()) {
                jedis.set(m.get_olt(), String.valueOf(worker_id));
            }
        }
        m.set_enqueued_at_olt(new Date().getTime());
        this.current_active_request.set(m.get_id());
        this.current_request_satisfied.put(m.get_id(), false);
        this.current_olts_request_channels.get(target_olt).basicPublish("", "requests", null, converter.toJson(m).getBytes(StandardCharsets.UTF_8));
        try {
            await().atMost(m.get_timeout(), TimeUnit.MILLISECONDS).until(request_satisfied(m.get_id()));
        } catch(Exception e) {
            log.info("⚠️ The request " + m.get_id() + " timeout!");
        }
    }
 
    private void start_olts_responses_consuming_logic() {
        Thread response_handler = new Thread(new ResponseConsumer(this.worker_queue_olt_response_channel, this.results_database_pool, this.redis_database_pool, this.current_active_request, this.current_request_satisfied)); 
        response_handler.start();
        try {
            response_handler.join();
        } catch(InterruptedException e) {
            log.info("❌ The response handler failed"); 
        }
    }

    private class ResponseConsumer implements Runnable {

        private Channel worker_queue_response_channel;
        private JedisPool results_database_pool;
        private JedisPool redis_database_pool;
        private AtomicInteger current_active_request;
        private Map<Integer, Boolean> current_request_satisfied;   

        Logger log = LoggerFactory.getLogger(this.getClass().getName());

        public ResponseConsumer(Channel worker_queue_response_channel, JedisPool results_database_pool, JedisPool redis_database_pool, AtomicInteger current_active_request, Map<Integer, Boolean> current_request_satisfied) {
            this.worker_queue_response_channel = worker_queue_response_channel;
            this.results_database_pool = results_database_pool;
            this.redis_database_pool = redis_database_pool;
            this.current_active_request = current_active_request;
            this.current_request_satisfied = current_request_satisfied;
        }

        public void run() {
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String jsonString = new String(delivery.getBody(), StandardCharsets.UTF_8);
                Response res = converter.fromJson(jsonString, Response.class);
                Message origin_message = res.get_origin_message();
                origin_message.set_completed(new Date().getTime());
                res.set_origin_message(origin_message);
                if(origin_message.get_id() == this.current_active_request.get()) {
                    res.set_timedout(false);
                    this.current_request_satisfied.put(origin_message.get_id(), true);
                } else {
                    res.set_timedout(true);
                }
                log.info("📥 Received response'" + converter.toJson(res) + " from OLT" + res.get_origin_message().get_olt());
                try(Jedis jedis = this.results_database_pool.getResource()) {
                    jedis.set(String.valueOf(res.get_origin_message().get_id()), converter.toJson(res));
                }
                try(Jedis jedis = this.redis_database_pool.getResource()) {
                    jedis.del(String.valueOf(origin_message.get_olt()));
                }
            };
            try {
                this.worker_queue_response_channel.basicConsume("responses", true, deliverCallback, consumerTag -> {});
            } catch(IOException e) {
                log.info("❌ Something has gone wrong while trying to declare the \"responses\" channel on the \"WORKER_QUEUE\"!");
            }
        }
    }
}
