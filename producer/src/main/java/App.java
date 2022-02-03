import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class App {

    @Parameter(names = { "-olts" , "--olt-count" }, description = "Number of different OLT's from which the messages may appear")
    private static int olt_number;

    @Parameter(names = { "-messages", "--message-count"}, description = "Number of consecutive messages to be generated")
    private static int messages_to_generate;

    @Parameter(names = { "-queue", "--queue-name" }, description = "Name of the queue to which the messages should be produced")
    private static String queue_name;

    @Parameter(names = { "-queue_host" }, description = "Where the queue is hosted")
    private static String queue_host;

    @Parameter(names = { "-queue_port"}, description = "The port in which the queue is exposed in the host")
    private static int queue_port;

    public static void main(String[] args) throws Exception {
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

    public void start() throws IOException, TimeoutException, InterruptedException {
        // Create a connection to the RabbitMQ server
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(queue_host);
        factory.setPort(queue_port);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(queue_name, false, false, false, null);
            for(int i = 0; i < messages_to_generate; i++) {
                Random rand = new Random();
                Message m = message_generator();
                // Sleep Time: 100 < t < 1000
                int sleep_time = (rand.nextInt(10) + 1) * 100;
                channel.basicPublish("", queue_name, null, m.get_olt().getBytes(StandardCharsets.UTF_8));
                System.out.println("Published '" + m.get_olt() + "' to the broker");
                Thread.sleep(sleep_time);
            }
        }
    }

    private static Message message_generator() {
        Random random = new Random();
        int olt_identifier = random.nextInt(olt_number + 1);
        int message_processing_time = random.nextInt(4) * 1000 + 1000; // 1000 < t < 4000
        String olt_name = "OLT" + olt_identifier;
        Message m = new Message(olt_name, message_processing_time);
        return m;
    }
}
