package pt.producer.utils;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import pt.producer.model.Message;
import pt.producer.model.Orchestration;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class Generator {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final Gson converter = new Gson();
    private Random random = new Random(34);

    Logger log = LoggerFactory.getLogger(this.getClass().getName());

    private List<Integer> generate_message_indexes(List<Integer> other_indexes, int messages) {
        List<Integer> message_indexes = new ArrayList<>();
        int number_of_indexes = (int) Math.floor(messages * 0.10); 
        for(int i = 0; i < number_of_indexes; i++) {
            int chosen_index = random.nextInt(messages);
            while(message_indexes.contains(chosen_index) || other_indexes.contains(chosen_index)) {
                chosen_index = random.nextInt(messages);
            }
            message_indexes.add(chosen_index);
        }
        return message_indexes;
    }

    private Message generate_message(int olt_count) {
        int olt_identifier = random.nextInt(olt_count);
        String olt_name = String.valueOf(olt_identifier);
        Message m = new Message(olt_name);
        return m;
    }

    public List<Message> generate_messages(Orchestration orchestration) {
        List<Message> generated_messages = new ArrayList<>();
        List<Integer> yellow_indexes = new ArrayList<>();
        List<Integer> red_indexes = new ArrayList<>();
        switch(orchestration.getSequence()) {
            case 2:
                yellow_indexes = generate_message_indexes(red_indexes, orchestration.getMessages());;
                break;
            case 3:
                yellow_indexes = generate_message_indexes(red_indexes, orchestration.getMessages());
                red_indexes = generate_message_indexes(yellow_indexes, orchestration.getMessages());
                break;
            default:
                break;
        }
        for(int i = 0; i < orchestration.getMessages(); i++) {
            Message m = generate_message(orchestration.getOlts());
            if(yellow_indexes.contains(i)) m.setYellowRequests(3);
            if(red_indexes.contains(i)) m.setRedRequests(3);
            generated_messages.add(m);
        }
        log.info("Used seed: 34 to generate messages");
        log.info("Generated messages: " + converter.toJson(generated_messages));
        return generated_messages;
    }
}
