package pt.producer.utils;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import pt.producer.model.Message;
import pt.producer.model.Orchestration;
import pt.producer.repository.MessageRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.List;

public class Generator {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final Gson converter = new Gson();

    Logger log = LoggerFactory.getLogger(this.getClass().getName());

    private Message generate_message(Random random, int olt_count) {
        int olt_identifier = random.nextInt(olt_count);
        String olt_name = String.valueOf(olt_identifier);
        Message m = new Message(olt_name);
        return m;
    }

    public List<Message> generate_messages(Orchestration orchestration) {
        int seed = 34;
        Random r = new Random(seed);
        List<Message> generated_messages = new ArrayList<>();
        for(int i = 0; i < orchestration.getMessages(); i++) {
            Message m = generate_message(r, orchestration.getOlts());
            generated_messages.add(m);
        }
        log.info("Used seed: " + seed + " to generate messages");
        return generated_messages;
    }
}
