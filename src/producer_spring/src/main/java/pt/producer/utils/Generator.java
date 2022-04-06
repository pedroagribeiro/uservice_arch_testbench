package pt.producer.utils;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pt.producer.model.Message;
import pt.producer.model.Orchestration;
import pt.producer.model.Status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
public class Generator {

    /**
     * NOTES ABOUT THE SIMULATION ENVIRONMENT
     *
     * 1 % of the messages will take longer than timeout to process [40000, 50000]
     * 3 % of the message will take either [10000, 15000, 19000] to process
     *
     * SEEDS
     *
     * Three seeds might be used:
     *
     * Seed 1: 42
     * Seed 2: 7
     * Seed 3: 34
     *
     * TODO: Replace placeholder URI by the brokers.
     */

    private final int message_timeout = 20000;
    private int message_id = 0;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final long[] longer_than_timeout_times = {40000, 50000};
    private static final long[] spike_times = {10000, 20000, 30000};
    private static final Gson converter = new Gson();

    Logger log = LoggerFactory.getLogger(this.getClass().getName());

    private Message generate_message(final int message_id, Random random, int olt_count, int type) {
        // Type 0: Random
        // Type 1: Processing time longer than timeout
        // Type 2: Spike
        int olt_identifier = random.nextInt(olt_count);
        long message_processing_time = 0;
        switch(type) {
            case 1:
                message_processing_time = longer_than_timeout_times[random.nextInt(2)];
                break;
            case 2:
                 message_processing_time = spike_times[random.nextInt(3)];
                 break;
            default:
                 message_processing_time = random.nextInt(4) * 1000 + 1000; // 1000 < t < 4000
                 break;
        }
        String olt_name = String.valueOf(olt_identifier);
        return new Message(message_id, olt_name, message_processing_time, message_timeout);
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
        int longer_than_timeout = (int) Math.floor(orchestration.get_messages() * 0.01);
        int spikes = (int) Math.floor(orchestration.get_messages() * 0.03);
        int seed = 34;
        Random r = new Random(seed);
        List<Integer> longer_than_timeouts_order_numbers = new ArrayList<>();
        for(int i = 0; i < longer_than_timeout; i++) {
            int order_number = r.nextInt(orchestration.get_messages());
            while(longer_than_timeouts_order_numbers.contains(order_number)) {
                order_number = r.nextInt(orchestration.get_messages());
            }
            longer_than_timeouts_order_numbers.add(order_number);
        }
        List<Integer> spikes_order_numbers = new ArrayList<>();
        for(int i = 0; i < spikes; i++) {
            int order_number = r.nextInt(orchestration.get_messages());
            while(longer_than_timeouts_order_numbers.contains(order_number) || spikes_order_numbers.contains(order_number)) {
                order_number = r.nextInt(orchestration.get_messages());
            }
            spikes_order_numbers.add(order_number);
        }
        for(int i = 0; i < orchestration.get_messages(); i++) {
            Message m;
            if(longer_than_timeouts_order_numbers.contains(i)) {
                m = generate_message(message_id++, r, orchestration.get_olts(), 1);
            } else if(spikes_order_numbers.contains(i)) {
                m = generate_message(message_id++, r, orchestration.get_olts(), 2);
            } else {
                m = generate_message(message_id++, r, orchestration.get_olts(), 0);
            }
            send_message_to_broker(m);
            int sleep_time = (r.nextInt(10) + 5) * 100;
            try {
                Thread.sleep(sleep_time);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info("Used seed: " + seed);
        return this.message_id;
    }
}
