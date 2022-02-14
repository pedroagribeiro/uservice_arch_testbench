import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    @Parameter(names = { "-id" }, description = "Identifier of the OLT")
    private static int id; 

    @Parameter(names = { "-containerized" }, description = "Describes if the running environment is fully containerized")
    private static boolean containerized;

    private String consuming_queue_host;
    private int consuming_queue_port;

    private Gson converter = new Gson();

    Logger log = LoggerFactory.getLogger(this.getClass().getName());

    public static void main(String[] args) throws Exception {
        App application = new App();
        JCommander commands = JCommander.newBuilder().addObject(application).build();
        try {
            commands.parse(args);
        } catch(Exception e) {
            commands.usage();
            System.exit(-1);
        }
        application.establish_environment_variables();
        application.start();
    }

    public void establish_environment_variables() {
        if(containerized) {
            this.consuming_queue_host = "olt" + id + "_queue";
            this.consuming_queue_port = 5672;
        } else {
            this.consuming_queue_host = "localhost";
            this.consuming_queue_port = 5676 + id;
        }
    }

    public Connection establish_connection_with_consuming_queue() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(this.consuming_queue_host);
        factory.setPort(this.consuming_queue_port);
        Connection connection = factory.newConnection();
        return connection;
    }

    public Channel declare_queue(Connection connection, final String queue_name) throws IOException {
        Channel channel = connection.createChannel();
        channel.queueDeclare(queue_name, false, false, false, null);
        return channel;
    }

    public void process_message(Message m, Channel producing_queue_channel) throws IOException {
        log.info("Received '" + converter.toJson(m) + "'"); 
        Response res = new Response(200, new Date().getTime(), -1, m);
        try {
            Thread.sleep(m.get_processing_time());
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        res.set_ended_handling(new Date().getTime());
        res.set_origin_message(m);
        producing_queue_channel.basicPublish("", "responses", null, converter.toJson(res).getBytes(StandardCharsets.UTF_8));
    }

    public void start() throws IOException, TimeoutException {
        Connection olt_queue_connection = establish_connection_with_consuming_queue();
        Channel consuming_queue_channel = declare_queue(olt_queue_connection, "requests");
        Channel producing_queue_channel = declare_queue(olt_queue_connection, "responses");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String jsonString = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Message m = converter.fromJson(jsonString, Message.class);
            m.set_dequeued_at_olt(new Date().getTime());
            process_message(m, producing_queue_channel);
        };
        consuming_queue_channel.basicConsume("requests", true, deliverCallback, consumerTag -> {});
    }
}
