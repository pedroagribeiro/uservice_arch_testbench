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

    @Parameter(names = { "-no_broker" }, description = "Describes if the broker process is being used")
    private static boolean no_broker; 

    @Parameter(names = { "-containerized" }, description = "Describes if the running environment is fully containerized")  
    private static boolean containerized;

    @Parameter(names = { "-olts" }, description = "The number of working OLT's")
    private static int olts;

    private String consuming_queue_host;
    private int consuming_queue_port;
    private String redis_database_host;
    private int redis_database_port;
    private String redis_results_database_host;
    private int redis_results_database_port;

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
        application.start_logic();
    }

    private void establish_environment_variables() {
        if(containerized) {
            if(no_broker) {
                this.consuming_queue_host = "broker_queue" + worker_id;
                this.consuming_queue_port = 5672;
                
            } else {
                this.consuming_queue_host = "worker_queue" + worker_id;
                this.consuming_queue_port = 5672;
            }
            this.redis_database_host = "redis_db";
            this.redis_database_port = 6379;
            this.redis_results_database_host = "results";
            this.redis_results_database_port = 6379;
        } else {
            this.consuming_queue_host = "localhost";
            if(no_broker) {
                this.consuming_queue_port = 5675;
            } else {
                this.consuming_queue_port = 5672 + worker_id;
            }
            this.redis_database_host = "localhost";
            this.redis_database_port = 6379;
            this.redis_results_database_host = "localhost";
            this.redis_results_database_port = 6380;
        }
    }

    public JedisPool establish_connection_with_redis_database() {
        return new JedisPool(redis_database_host, redis_database_port);
    }

    public JedisPool establish_connection_with_redis_results_database() {
        return new JedisPool(redis_results_database_host, redis_results_database_port);
    }

    public Connection establish_connection_with_worker_queue() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(consuming_queue_host);
        factory.setPort(consuming_queue_port);
        Connection connection = factory.newConnection();
        return connection;
    }

    public List<Connection> establish_connection_with_olts_queues() throws IOException, TimeoutException {
        List<Connection> olts_queues_connections = new ArrayList<>();
        for(int i = 0; i < olts; i++) {
            ConnectionFactory factory = new ConnectionFactory();
            if(containerized) {
                factory.setHost("olt" + worker_id + "_queue");
                factory.setPort(5672);
            } else {
                factory.setHost("localhost");
                factory.setPort(5676 + worker_id);
            }
            Connection connection = factory.newConnection();
            olts_queues_connections.add(connection);
        }
        return olts_queues_connections;
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

    public void process_message(Message m, JedisPool redis_database_pool, List<Channel> olts_queues_channels, boolean no_broker) throws IOException {
        int target_olt = Integer.parseInt(m.get_olt());
        log.info("Received: '" + converter.toJson(m) + "'");
        if(no_broker) {
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
        olts_queues_channels.get(target_olt).basicPublish("", "requests", null, converter.toJson(m).getBytes(StandardCharsets.UTF_8));
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
 
    public void start_olts_responses_consuming_logic(List<Connection> olts_queues_connections, JedisPool redis_results_database_pool) throws IOException {
        List<Channel> olts_queues_response_channels = declare_queues(olts_queues_connections, "responses");
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
        for(Channel olts_queue_response_channel : olts_queues_response_channels) {
            olts_queue_response_channel.basicConsume("responses", true, deliverCallback, consumerTag -> {});
        }
    }

    public void start_logic() throws IOException, TimeoutException, InterruptedException {
        JedisPool redis_database_pool = establish_connection_with_redis_database();
        JedisPool redis_results_database_pool = establish_connection_with_redis_results_database();
        Connection worker_queue_connection = establish_connection_with_worker_queue();
        Channel worker_queue_channel = declare_queue(worker_queue_connection, "message_queue");
        List<Connection> olts_queues_connections = establish_connection_with_olts_queues();
        List<Channel> olts_queues_channels_requests = declare_queues(olts_queues_connections, "requests");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String jsonString = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Message m = converter.fromJson(jsonString, Message.class);
            m.set_dequeued_at_worker(new Date().getTime());
            process_message(m, redis_database_pool, olts_queues_channels_requests, no_broker);
        };
        worker_queue_channel.basicConsume("message_queue", true, deliverCallback, consumerTag -> {});
        start_olts_responses_consuming_logic(olts_queues_connections, redis_results_database_pool);
    }
    
}
