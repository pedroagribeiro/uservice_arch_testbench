package pt.testbench.broker_spring.handlers;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pt.testbench.broker_spring.model.Message;
import pt.testbench.broker_spring.model.Status;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

@Service
@Slf4j
public class ReceiveMessageHandler {

    private final Gson converter = new Gson();
    private final MessageDigest digester = MessageDigest.getInstance("SHA-256");
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private Status status;

    @Value("${spring.base_worker.host")
    private String base_worker_host;

    public ReceiveMessageHandler() throws NoSuchAlgorithmException {
    }

    private void forward_message_to_worker(Message m, int worker, int logic) {
        String worker_host = this.base_worker_host;
        if(!worker_host.equals("localhost")) worker_host = worker_host + worker;
        int worker_port = 8500 + worker;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Message> entity = new HttpEntity<>(m, headers);
        ResponseEntity<?> response = restTemplate.exchange("http://" + worker_host + ":" + worker_port + "/message", HttpMethod.POST, entity, String.class);
        if(response.getStatusCode().isError()) {
            log.info("The message could not be forwarded to the worker, something went wrong!");
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("Forwarded " + converter.toJson(m) + " to worker " + worker + " using logic " + logic);
            }
        }
    }

    public void process_message_architecture_3(Message message) {
        byte[] diggested_message = this.digester.digest(message.get_olt().getBytes(StandardCharsets.UTF_8));
        int worker_to_forward = (int) new BigInteger(diggested_message).longValue() % status.getWorkers();
        if (worker_to_forward < 0 || worker_to_forward > status.getWorkers() - 1) {
            worker_to_forward = new Random().nextInt(status.getWorkers());
        }
        message.set_worker(worker_to_forward);
        forward_message_to_worker(message, worker_to_forward, 3);
    }

    public void process_message_architecture_4(Message message) {
        int worker_to_forward = 0;
        if(status.getOracle().containsKey(message.get_olt())) {
            worker_to_forward = status.getOracle().get(message.get_olt());
        } else {
            worker_to_forward = (status.getLastChosenWorker() + 1) % status.getWorkers();
        }
        status.setLastChosenWorker(worker_to_forward);
        message.set_worker(worker_to_forward);
        forward_message_to_worker(message, worker_to_forward, 4);
    }

    public void handleMessage(String body) {
        Message message = converter.fromJson(body, Message.class);
        message.set_dequeued_at_broker(new Date().getTime());
        log.info("Received message: " + converter.toJson(message));
        switch(status.getArchitecture()) {
            case 3:
                process_message_architecture_3(message);
                break;
            case 4:
                process_message_architecture_4(message);
                break;
            default:
                log.info("Consuming messages when it shouldn't");
                break;
        }
    }

}
