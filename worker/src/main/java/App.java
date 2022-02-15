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
import java.util.concurrent.TimeoutException;

public class App {

    @Parameter(names = { "-id" }, description = "The identifier of the current worker")
    private static int worker_id;

    @Parameter(names = { "-containerized" }, description = "Describes if the running environment is fully containerized")  
    private static boolean containerized;

    private String worker_queue_host = "localhost";
    private int worker_queue_port = 5672 + worker_id;
    private String broker_queue_host = "localhost";
    private int broker_queue_port = 5675;
    private String redis_database_host = "localhost";
    private int redis_database_port = 6379;
    private String redis_results_database_host = "localhost";
    private int redis_results_database_port = 6380;

    private final static int WORKER_QUEUE = 0;
    private final static int BROKER_QUEUE = 1;

    private int current_consumption = WORKER_QUEUE;
    private Connection current_consumption_connection;
    private Channel current_consumption_channel;
    private Channel orchestration_channel;

    private List<Connection> current_olts_connections = new ArrayList<>();
    private List<Channel> current_olts_message_channels;
    private List<Channel> current_olts_response_channels;

    private static Gson converter = new Gson();

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
        application.establish_environment_variables();
        application.run();
    }

    private void establish_environment_variables() {
        if(containerized) {
            this.worker_queue_host = "worker_queue" + worker_id;
            this.worker_queue_port = 5672;
            this.broker_queue_host = "broker_queue";
            this.broker_queue_port = 5672;
            this.redis_database_host = "redis_db";
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

    public JedisPool establish_connection_with_redis_database() {
        log.info("🕋 Connecting to the \"REDIS DATABASE\"...");
        JedisPool pool = null;
        pool = new JedisPool(redis_database_host, redis_database_port);
        log.info("✅ Successfuly connected to the \"REDIS DATABASE\"!");
        return pool;
    }

    public JedisPool establish_connection_with_redis_results_database() {
        log.info("🕋 Connecting to the \"RESULTS DATABASE\"...");
        JedisPool pool = null;
        pool = new JedisPool(redis_results_database_host, redis_results_database_port);
        log.info("✅ Successfuly connected to the \"RESULTS DATABASE\"!");
        return pool;
    }

    public Connection establish_connection_with_worker_queue() throws IOException, TimeoutException {
        log.info("🕋 Connecting to the \"WORKER QUEUE\"...");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(worker_queue_host);
        factory.setPort(worker_queue_port);
        Connection connection = factory.newConnection();
        log.info("✅ Successfuly connected to the \"WORKER QUEUE\"!");
        return connection;
    }

    public Connection establish_connection_with_consuming_queue(Orchestration orchestration) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = null;
        if(orchestration.get_no_broker() == true) {
            log.info("🕋 Connecting to the \"BROKER QUEUE\"...");
            factory.setHost(broker_queue_host);
            factory.setPort(broker_queue_port);
            connection = factory.newConnection();
            log.info("✅ Successfuly connected to the \"BROKER QUEUE\"!");
        } else {
            log.info("🕋 Connectiong to the \"WORKER_QUEUE\"...");
            factory.setHost(worker_queue_host);
            factory.setPort(worker_queue_port);
            connection = factory.newConnection();
            log.info("✅ Successfuly connected to the \"WORKER QUEUE\"!");
        } 
        return connection;
    }

    public void establish_connection_with_olts_queues(Orchestration orchestration) throws IOException, TimeoutException {
        log.info("🕋 Connecting to the \"OLT'S QUEUES\"...");
        if(current_olts_message_channels != null) {
            for(Channel olt_channel : current_olts_message_channels) {
                olt_channel.close();
            }
        }
        if(current_olts_connections != null) {
            for(Connection olt_connection : current_olts_connections) {
                olt_connection.close();
            }
        }
        for(int i = 0; i < orchestration.get_olts(); i++) {
            ConnectionFactory factory = new ConnectionFactory();
            if(containerized) {
                factory.setHost("olt" + i + "_queue");
                factory.setPort(5672);
            } else {
                factory.setHost("localhost");
                factory.setPort(5676 + i);
            }
            Connection connection = factory.newConnection();
            current_olts_connections.add(connection);
        }
        current_olts_message_channels = declare_queues(current_olts_connections, "message_queue");
        current_olts_response_channels = declare_queues(current_olts_connections, "responses");
        log.info("✅ Successfuly connected to the \"OLT'S QUEUES\"!");
    }

    public List<Channel> declare_queues(List<Connection> connections, final String queue_name) throws IOException {
        List<Channel> channels = new ArrayList<>();
        for(Connection connection : connections) {
            Channel channel = connection.createChannel();
            channel.queueDeclare(queue_name, false, false, false, null);
            channels.add(channel);
        }
        return channels;
    }

    public Channel declare_queue(Connection connection, String queue_name) throws IOException {
        Channel channel = connection.createChannel();
        channel.queueDeclare(queue_name, false, false, false, null);
        return channel;
    }

    public void process_message(Message m, JedisPool redis_database_pool) throws IOException {
        int target_olt = Integer.parseInt(m.get_olt());
        log.info("Received: '" + converter.toJson(m) + "'");
        if(current_consumption == BROKER_QUEUE) {
            try(Jedis jedis = redis_database_pool.getResource()) {
                boolean handling_request_for_same_olt = jedis.exists(m.get_olt());
                while(handling_request_for_same_olt) {
                    try {
                        Thread.sleep(100);
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                    handling_request_for_same_olt = jedis.exists(m.get_olt());
                }
                jedis.set(m.get_olt(), String.valueOf(worker_id));
            }
        }
        m.set_enqueued_at_olt(new Date().getTime());
        current_olts_message_channels.get(target_olt).basicPublish("", "requests", null, converter.toJson(m).getBytes(StandardCharsets.UTF_8));
        long sleep_time = (m.get_processing_time() > m.get_timeout()) ? m.get_timeout() : m.get_processing_time();
        try {
            Thread.sleep(sleep_time);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        try(Jedis jedis = redis_database_pool.getResource()) {
            jedis.del(m.get_olt());
        }

    }
 
    public void start_olts_responses_consuming_logic(JedisPool redis_results_database_pool) throws IOException {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String jsonString = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Response res = converter.fromJson(jsonString, Response.class);
            Message origin_message = res.get_origin_message();
            origin_message.set_completed(new Date().getTime());
            res.set_origin_message(origin_message);
            long roundtrip_time = res.get_origin_message().get_completed() - res.get_origin_message().get_enqueued_at_olt();
            log.info("Roundtrip Time: " + roundtrip_time);
            if(roundtrip_time > res.get_origin_message().get_timeout()) {
                res.set_timedout(true);
            } else {
                res.set_timedout(false);
            }
            log.info("Received response'" + converter.toJson(res) + " from OLT" + res.get_origin_message().get_olt());
            try(Jedis jedis = redis_results_database_pool.getResource()) {
                jedis.set(String.valueOf(res.get_origin_message().get_id()), converter.toJson(res));
            }
        };
        for(Channel olts_queue_response_channel : current_olts_response_channels) {
            olts_queue_response_channel.basicConsume("responses", true, deliverCallback, consumerTag -> {});
        }
    }

    private void setup_orchestration_consumer(JedisPool redis_database_pool, JedisPool redis_results_database_pool) throws IOException {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            log.info("🔀 Got new orchestration request...");
            String jsonString = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Orchestration orchestration = converter.fromJson(jsonString, Orchestration.class);
            boolean changed_connection = false;
            try {
                if(current_consumption_connection == null || current_consumption_channel == null) {
                    changed_connection = true;
                }
                if(orchestration.get_no_broker() == true) {
                    if(current_consumption == WORKER_QUEUE) {
                        current_consumption = BROKER_QUEUE;
                        if(current_consumption_channel != null) current_consumption_channel.close();
                        if(current_consumption_connection != null) current_consumption_connection.close();
                        changed_connection = true;
                    } 
                } else {
                    if(current_consumption == BROKER_QUEUE) {
                        current_consumption = WORKER_QUEUE;
                        if(current_consumption_channel != null) current_consumption_channel.close();
                        if(current_consumption_connection != null) current_consumption_connection.close();
                        changed_connection = true;
                    }
                }
                if(changed_connection == true) {
                    current_consumption_connection = establish_connection_with_consuming_queue(orchestration);
                    current_consumption_channel = declare_queue(current_consumption_connection, "message_queue");
                }
            } catch(TimeoutException e) {
                e.printStackTrace();
            }
            try {
                establish_connection_with_olts_queues(orchestration);
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            start_olts_responses_consuming_logic(redis_results_database_pool);
            start_message_consuming_logic(redis_database_pool);
            log.info("✅ The new orchestration request imposed changes are now in effect!"); 
        };
        orchestration_channel.basicConsume("orchestration", true, deliverCallback, consumerTag -> {});
    }

    private void start_message_consuming_logic(JedisPool redis_database_pool) throws IOException {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String jsonString = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Message m = converter.fromJson(jsonString, Message.class);
            m.set_dequeued_at_worker(new Date().getTime());
            process_message(m, redis_database_pool);
        };
        current_consumption_channel.basicConsume("message_queue", true, deliverCallback, consumerTag -> {});
    }

    public void run() throws IOException, TimeoutException, InterruptedException {
        JedisPool redis_database_pool = establish_connection_with_redis_database();
        JedisPool redis_results_database_pool = establish_connection_with_redis_results_database();
        Connection worker_queue_connection = establish_connection_with_worker_queue();
        orchestration_channel = declare_queue(worker_queue_connection, "orchestration");
        setup_orchestration_consumer(redis_database_pool, redis_results_database_pool);
    }
    
}
