import java.util.Random;

public class Producer {

    private static int olt_number = 100;
    private static int messages_to_generate = 10000;

    public static void main(String[] args) throws Exception {
        for(int i = 0; i < messages_to_generate; i++) {
            Random rand = new Random();
            Message m = message_generator();
            System.out.println(m);
            // Sleep Time: 100 < t < 1000
            int sleep_time = (rand.nextInt(10) + 1) * 100;
            Thread.sleep(sleep_time);
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
