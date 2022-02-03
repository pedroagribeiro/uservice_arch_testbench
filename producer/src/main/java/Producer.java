import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Producer {

    private static int olt_number = 100;
    private static int messages_to_generate = 10000;
    private final static String QUEUE_NAME = "message_queue";

    public static void main(String[] args) throws Exception {

        // Create a connection to the RabbitMQ server
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            for(int i = 0; i < messages_to_generate; i++) {
               Random rand = new Random();
               Message m = message_generator();
               // Sleep Time: 100 < t < 1000
               int sleep_time = (rand.nextInt(10) + 1) * 100;
               channel.basicPublish("", QUEUE_NAME, null, m.get_olt().getBytes());
               System.out.println("[x] Sent '" + m.get_olt() + "'");
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
