package pt.producer.utils;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import pt.producer.model.Message;
import pt.producer.model.Orchestration;
import pt.producer.repository.MessageRepository;

import java.util.Collections;
import java.util.Random;

public class Generator {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final Gson converter = new Gson();

    private MessageRepository messagesRepository;

    Logger log = LoggerFactory.getLogger(this.getClass().getName());

    public Generator(MessageRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    private Message generate_message(Random random, int olt_count) {
        int olt_identifier = random.nextInt(olt_count);
        String olt_name = String.valueOf(olt_identifier);
        Message m = new Message(olt_name);
        this.messagesRepository.save(m);
        return this.messagesRepository.findMessageWithHighestId().get(0);
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

    public void generate_messages(Orchestration orchestration) {
        int seed = 34;
        Random r = new Random(seed);
        for(int i = 0; i < orchestration.getMessages(); i++) {
            Message m = generate_message(r, orchestration.getOlts());
            send_message_to_broker(m);
            int sleep_time = (r.nextInt(10) + 5) * 100;
            try {
                Thread.sleep(sleep_time);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info("Used seed: " + seed + " to generate messages");
    }
}
