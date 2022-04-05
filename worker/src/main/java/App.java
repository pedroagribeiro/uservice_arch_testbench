import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import com.rabbitmq.client.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

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

    // This should be set to how many OLT containers there are in the deployment
    private static final int OLT_CONTAINERS = 5;

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

    private boolean wait = true;

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

    private AtomicInteger received_responses = new AtomicInteger(0);

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
            this.redis_results_database_host = "results-db";
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
        log.info("SUCCESS: Successfuly connected to the redis database");
    }

    public void establish_connection_with_redis_results_database() {
        log.info("STATUS: Connecting to the results database at: " + this.redis_results_database_host + ":" + this.redis_results_database_port);
        boolean success = false;
        while(success == false) {
            this.results_database_pool = new JedisPool(this.redis_results_database_host, this.redis_results_database_port);
            try {
                Jedis jedis = this.results_database_pool.getResource();
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
        log.info("SUCCESS: Successfuly connected to the results database");
    }

    public void establish_connection_with_worker_queue() {
        log.info("STATUS: Connecting to the worker queue at: " + this.worker_queue_host + ":" + this.worker_queue_port);
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(this.worker_queue_host);
        factory.setPort(this.worker_queue_port);
        while(this.worker_queue_connection == null) {
            try {
                this.worker_queue_connection = factory.newConnection();
                log.info("SUCCESS: Connected to the worker queue");
            } catch(IOException | TimeoutException e) {
                try {
                    Thread.sleep(3000);
                } catch(InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void establish_connection_with_broker_queue() {
        log.info("STAUTS: Connecting to the broker queue");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(broker_queue_host);
        factory.setPort(broker_queue_port);
        while(this.broker_queue_connection == null) {
            try {
                this.broker_queue_connection = factory.newConnection();
                log.info("SUCCESS: Connected to the broker queue");
            } catch(IOException | TimeoutException e) {
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
            log.info("FAILURE: Something went wrong while declaring the \"orchestration\" channel on the worker queue");
        }
        try {
            this.worker_queue_olt_response_channel = this.worker_queue_connection.createChannel();
            this.worker_queue_olt_response_channel.queueDeclare("responses", false, false, false, null);
        } catch(IOException e) {
            log.info("FAILURE: Something went wrong while declaring the \"responses\" channel on the worker queue");
        }
    }

    public void establish_connection_with_olts_queues() {
        log.info("STATUS: Connecting to the OLT's queues");
        for(int i = 0; i < App.OLT_CONTAINERS; i++) {
            ConnectionFactory factory = new ConnectionFactory();
            if(containerized) {
                factory.setHost("olt-queue" + i);
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
                    try {
                        Thread.sleep(3000);
                    } catch(InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        log.info("SUCCESS: Connected to the OLT's queues");
    }

    public void establish_olts_queues_channels(int olts) {
        log.info("STATUS: Creating channels for communication with the OLT's");
        if(this.current_olts_request_channels != null) {
            for(int i = 0; i < this.current_olts_request_channels.size(); i++) {
                try {
                    this.current_olts_request_channels.get(i).close();
                } catch(IOException | TimeoutException e) {
                    log.info("FAILURE: An error ocurred while closing the \"requests\" channel on OLT " + i);
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
                log.info("FAILURE: An error ocurred while creating the \"requests\" channel on OLT " + i);
            }
        }
        log.info("SUCCESS: Created channels for communication with the OLT's");
    }

    public void establish_current_consuming_channel() {
        if(this.current_consumption_channel != null) {
            try {
                this.current_consumption_channel.close();
            } catch(IOException | TimeoutException e) {
                log.info("FAILURE: Something has gone wrong while trying to close the \"message_queue\" channel on the consuming queue");
            }
        }
        try {
            if(this.current_consumption == WORKER_QUEUE) {
                this.current_consumption_channel = this.worker_queue_connection.createChannel();
            } else {
                this.current_consumption_channel = this.broker_queue_connection.createChannel();
            }
            this.current_consumption_channel.queueDeclare("message_queue", false, false, false, null);
        } catch(IOException e) {
            log.info("FAILURE: Something has gone wrong while trying to declare the \"message_queue\" on the consuming queue");
        } 
    }

    public void setup_message_consumption() {
        DeliverCallback deliverCallback = (consumerTag, deliver) -> {
            String jsonString = new String(deliver.getBody(), StandardCharsets.UTF_8);
            Message m = App.converter.fromJson(jsonString, Message.class);
            if(current_consumption == BROKER_QUEUE) {
                m.set_dequeued_at_broker(new Date().getTime());
                m.set_enqueued_at_worker(new Date().getTime());
                m.set_dequeued_at_worker(m.get_enqueued_at_worker());
            } else {
                m.set_dequeued_at_worker(new Date().getTime());
            }
            m.set_worker(worker_id);
            process_message(m);
        };
        try {
            this.current_consumption_channel.basicConsume("message_queue", true, deliverCallback, consumerTag -> {});
        } catch(IOException e) {
            log.info("FAILURE: Something has gone wrong when trying to consume from \"message_queue\" channel in the consuming queue");
        }
    }

    private void setup_orchestration_consumption() {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            log.info("STATUS: Got new orchestration request");
            String jsonString = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Orchestration orchestration = converter.fromJson(jsonString, Orchestration.class);
            this.received_responses.set(0);
            if(orchestration.get_algorithm() == 3 || orchestration.get_algorithm() == 4) {
                if(App.worker_id <= orchestration.get_workers() - 1) {
                    if(this.current_consumption == WORKER_QUEUE) {
                        this.current_consumption = BROKER_QUEUE;
                        establish_current_consuming_channel();
                    } 
                    if(orchestration.get_algorithm() == 4) {
                        this.wait = false;
                    }
                }
            } else {
                if(this.current_consumption == BROKER_QUEUE) {
                    this.current_consumption = WORKER_QUEUE;
                    establish_current_consuming_channel();
                }
            }
            establish_olts_queues_channels(orchestration.get_olts());
            setup_message_consumption();
            start_olts_responses_consuming_logic();
            log.info("Connected to " + this.current_olts_request_channels.size() + " OLT's");
            log.info("SUCCESS: The changes imposed by the orchestration request are now in effect"); 
        };
        try {
            this.worker_queue_orchestration_channel.basicConsume("orchestration", true, deliverCallback, consumerTag -> {});
        } catch(IOException e) {
            log.info("FAILURE: Something has gone wrong when trying to consume from \"orchestration\" channel in the worker queue");
        }
    }

    public Connection establish_connection_with_consuming_queue(Orchestration orchestration) {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = null;
        if(orchestration.get_algorithm() == 3 || orchestration.get_algorithm() == 4) {
            log.info("STATUS: Connecting to the broker queue");
            factory.setHost(broker_queue_host);
            factory.setPort(broker_queue_port);
            try {
                connection = factory.newConnection();
                log.info("SUCCESS: Connected to the broker queue");
            } catch(IOException | TimeoutException e) {
                log.info("FAILURE: Could not connect to the broker queue");
            }
        } else {
            log.info("STATUS: Connectiong to the worker queue");
            factory.setHost(worker_queue_host);
            factory.setPort(worker_queue_port);
            try {
                connection = factory.newConnection();
                log.info("SUCCESS: Connected to the worker queue");
            } catch(IOException | TimeoutException e) {
                log.info("FAILURE: Could not connect to the worker queue");
            }
        } 
        return connection;
    }

    private Callable<Boolean> request_satisfied(int request_id) {
        return () -> this.current_request_satisfied.get(request_id);
    }

    public void process_message(Message m) throws IOException {
        int target_olt = Integer.parseInt(m.get_olt());
        log.info("STATUS: Received: '" + converter.toJson(m) + "'");
        if(this.current_consumption == BROKER_QUEUE) {
            if(this.wait) {
                try(Jedis jedis = this.redis_database_pool.getResource()) {
                    boolean handling_request_for_same_olt = jedis.exists(m.get_olt());
                    while(handling_request_for_same_olt) {
                        try {

                            log.info("Waiting... " + " Worker " + jedis.get(m.get_olt()) + " is handling request for olt " + m.get_olt());
                            Thread.sleep(100);
                        } catch(InterruptedException e) {
                            e.printStackTrace();
                        }
                        handling_request_for_same_olt = jedis.exists(m.get_olt());
                    }
                    jedis.set(m.get_olt(), String.valueOf(worker_id));
                }
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
            log.info("TIMEOUT: The request " + m.get_id() + " timedout");
            try(Jedis jedis = this.redis_database_pool.getResource()) {
                jedis.del(String.valueOf(m.get_olt()));
            }
        }
    }

    private void start_olts_responses_consuming_logic() {
        Thread response_handler = new Thread(new ResponseConsumer(this.worker_queue_olt_response_channel, this.results_database_pool, this.redis_database_pool, this.current_active_request, this.current_request_satisfied, this.received_responses)); 
        response_handler.start();
        try {
            response_handler.join();
        } catch(InterruptedException e) {
            log.info("FAILURE: The OLT response handler thread failed"); 
        }
    }

    private class ResponseConsumer implements Runnable {

        private Channel worker_queue_response_channel;
        private JedisPool results_database_pool;
        private JedisPool redis_database_pool;
        private AtomicInteger current_active_request;
        private Map<Integer, Boolean> current_request_satisfied;   
        private AtomicInteger received_responses;

        Logger log = LoggerFactory.getLogger(this.getClass().getName());

        public ResponseConsumer(Channel worker_queue_response_channel, JedisPool results_database_pool, JedisPool redis_database_pool, AtomicInteger current_active_request, Map<Integer, Boolean> current_request_satisfied, AtomicInteger received_responses) {
            this.worker_queue_response_channel = worker_queue_response_channel;
            this.results_database_pool = results_database_pool;
            this.redis_database_pool = redis_database_pool;
            this.current_active_request = current_active_request;
            this.current_request_satisfied = current_request_satisfied;
            this.received_responses = received_responses;
        }

        public void run() {
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String jsonString = new String(delivery.getBody(), StandardCharsets.UTF_8);
                Response res = converter.fromJson(jsonString, Response.class);
                this.received_responses.set(this.received_responses.get() + 1);
                Message origin_message = res.get_origin_message();
                origin_message.set_completed(new Date().getTime());
                res.set_origin_message(origin_message);
                if(origin_message.get_id() == this.current_active_request.get()) {
                    res.set_timedout(false);
                    this.current_request_satisfied.put(origin_message.get_id(), true);
                } else {
                    res.set_timedout(true);
                }
                log.info("STATUS: Received response'" + converter.toJson(res) + " from OLT" + res.get_origin_message().get_olt());
                try(Jedis jedis = this.results_database_pool.getResource()) {
                    jedis.set(String.valueOf(res.get_origin_message().get_id()), converter.toJson(res));
                } catch(Exception e) {
                    log.info("FAILURE: Couldn't write request result");
                    e.printStackTrace();
                }
                try(Jedis jedis = this.redis_database_pool.getResource()) {
                    jedis.del(String.valueOf(origin_message.get_olt()));
                }
            };
            try {
                this.worker_queue_response_channel.basicConsume("responses", true, deliverCallback, consumerTag -> {});
            } catch(IOException e) {
                log.info("FAILURE: Something has gone wrong while trying to declare the \"responses\" channel on the worker queue");
            }
        }
    }
}
