package pt.producer.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.producer.model.Message;
import pt.producer.model.Orchestration;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class Generator {

    private Random random = new Random(34);
    private String last_olt = null;
    private int left_messages_to_generate = 0;


    Logger log = LoggerFactory.getLogger(this.getClass().getName());

    private boolean generate_boolean_based_on_percentage(int percentage_true) {
        int generated_number = this.random.nextInt(100);
        if(generated_number <= percentage_true) {
            this.left_messages_to_generate = 3;
            return true;
        }
        return false;
    }

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
            if(this.left_messages_to_generate > 0) {
                if(this.last_olt == null) {
                    m.setOlt(this.last_olt);
                } else {
                    int olt = this.random.nextInt(orchestration.getOlts());
                    this.last_olt = String.valueOf(olt);
                }
                this.left_messages_to_generate--;
            } else {
                boolean new_same_worker_sequence = generate_boolean_based_on_percentage(20);
                if(new_same_worker_sequence) this.left_messages_to_generate = 3;
                this.last_olt = m.getOlt();
            }
            if(yellow_indexes.contains(i)) m.setYellowRequests(3);
            if(red_indexes.contains(i)) m.setRedRequests(3);
            generated_messages.add(m);
        }
        return generated_messages;
    }
}
