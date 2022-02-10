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

    @Parameter(names = { "-queue_host"}, description = "Host of the queue from which the process reads and writes")
    private static String queue_host;

    @Parameter(names = { "-queue_port" }, description = "Port of the queue from which the process reads and writes")
    private static int queue_port;

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
        try {
        Thread.sleep(10000);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        application.start();
    }

    public void start() throws IOException, TimeoutException {
        // Connection to it's own queue to consume the incoming requests 
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(queue_host);
        factory.setPort(5676 + id);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("requests", false, false, false, null);
        channel.queueDeclare("responses", false, false, false, null);
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String jsonString = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Message m = converter.fromJson(jsonString, Message.class);
            log.info("Received: '" + converter.toJson(m) + "'");
            try {
                Thread.sleep(m.get_processing_time());
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            Response response = new Response();
            response.set_started_handling(new Date().getTime());
            try {
                Thread.sleep(m.get_processing_time());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            response.set_status(200);
            response.set_ended_handling(new Date().getTime());
            channel.basicPublish("", "responses", null, converter.toJson(response).getBytes(StandardCharsets.UTF_8));
        };
        channel.basicConsume("requests", true, deliverCallback, consumerTag -> {});
    }
}
