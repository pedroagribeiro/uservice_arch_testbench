package pt.producer.utils;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pt.producer.model.Message;
import pt.producer.model.Orchestration;
import pt.producer.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Collections;
import java.util.Random;

public class Generator {

    private int message_id = 0;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final Gson converter = new Gson();

    Logger log = LoggerFactory.getLogger(this.getClass().getName());

    private Message generate_message(final int message_id, Random random, int olt_count) {
        int olt_identifier = random.nextInt(olt_count);
        String olt_name = String.valueOf(olt_identifier);
        return new Message(message_id, olt_name);
    }

    public void send_message_to_broker(Message m) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Message> entity = new HttpEntity<>(m, headers);
        ResponseEntity<?> response = restTemplate.exchange("http://broker:8081/message", HttpMethod.POST, entity, String.class);
        if(response.getStatusCode().isError()) {
            log.error("The message could not be sent to the broker, something went wrong!");
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("Sent " + converter.toJson(m) + " to the broker.");
            }
        }
    }

    public int generate_messages(int messageId, Orchestration orchestration) {
        this.message_id = messageId;
        int seed = 34;
        Random r = new Random(seed);
        for(int i = 0; i < orchestration.getMessages(); i++) {
            Message m = generate_message(message_id++, r, orchestration.getOlts());
            send_message_to_broker(m);
            int sleep_time = (r.nextInt(10) + 5) * 100;
            try {
                Thread.sleep(sleep_time);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info("Used seed: " + seed + " to generate messages");
        return this.message_id;
    }
}
