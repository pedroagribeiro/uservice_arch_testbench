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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class App {

    @Parameter(names = { "-queue", "--queue_name"}, description = "The identifier of the queue from which the broker should consume")
    private static String queue_name;

    @Parameter(names = { "-workers", "--workers_count"}, description = "Number of workers that are running")
    private static int upstream_workers_count;

    @Parameter(names = { "-redis_host" }, description = "Host of the Redis database")
    private static String redis_host;

    @Parameter(names = { "-redis_port" }, description = "Port in which the database is exposed on the host")
    private static int redis_port;

    @Parameter(names = { "-workers_base_port" }, description = "Base port of the workers message queues")
    private static int base_workers_port;

    @Parameter(names = { "-queue_host" }, description = "Consumer queue host")
    private static String queue_host;

    @Parameter(names = { "-queue_port"}, description = "Consumer queue port")
    private static int queue_port;

    private Gson converter = new Gson();

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

    public void start() throws IOException, TimeoutException {
        // Round-robin setup
        AtomicInteger last_chosen_worker = new AtomicInteger(upstream_workers_count - 1);
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
        for(int i = 0; i < upstream_workers_count; i++) {
            ConnectionFactory fac = new ConnectionFactory();
            fac.setHost("localhost");
            fac.setPort(base_workers_port + i);
            Connection prod_connection = fac.newConnection();
            Channel prod_channel = prod_connection.createChannel();
            channel.queueDeclare(queue_name, false, false, false, null);
            producing_channels.add(prod_channel);
        }

        channel.queueDeclare(queue_name, false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String jsonString = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Message m = converter.fromJson(jsonString, Message.class);
            try (Jedis jedis = pool.getResource()) {
                if(jedis.exists(m.get_olt())) {
                    int worker = Integer.parseInt(jedis.get(m.get_olt()));
                    m.set_forwarded_by_broker(new Date().getTime());
                    producing_channels.get(worker).basicPublish("", queue_name, null, converter.toJson(m).getBytes(StandardCharsets.UTF_8));
                    log.info("Forwarded '" + converter.toJson(m) + "' to worker no. " + worker);
                } else {
                    int worker = (last_chosen_worker.get() + 1) % upstream_workers_count;
                    m.set_forwarded_by_broker(new Date().getTime());
                    last_chosen_worker.set(worker);
                    producing_channels.get(worker).basicPublish("", queue_name, null, converter.toJson(m).getBytes(StandardCharsets.UTF_8));
                    log.info("Forwarded '" + converter.toJson(m) + "' to worker no. " + worker);
                }
            }
        };
        channel.basicConsume(queue_name, true, deliverCallback, consumerTag -> {});
    }
}
