import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeoutException;

public class App {

    @Parameter(names = { "-queue", "--queue_name" }, description = "The identifier queue from which the program is going to consume")
    private static String queue_name;

    @Parameter(names = { "-id", "--worker-id" }, description = "The identifier of the current worker")
    private static int worker_id;

    private static Gson converter = new Gson();

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
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672 + worker_id);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(queue_name, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String jsonString = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Message m = converter.fromJson(jsonString, Message.class);
            m.set_handled_by_worker(new Date());
            System.out.println(" [x] Received: '" + converter.toJson(m) + "'");
        };

        channel.basicConsume(queue_name, true, deliverCallback, consumerTag -> {});
    }
}
