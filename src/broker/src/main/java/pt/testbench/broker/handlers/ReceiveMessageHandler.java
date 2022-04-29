package pt.testbench.broker.handlers;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import pt.testbench.broker.communication.Worker;
import pt.testbench.broker.model.Message;
import pt.testbench.broker.model.Status;
import java.nio.charset.StandardCharsets;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Random;

@Service
public class ReceiveMessageHandler {

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private final Gson converter = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
    private final MessageDigest digester = MessageDigest.getInstance("SHA-256");

    public ReceiveMessageHandler() throws NoSuchAlgorithmException {
    }

    @Autowired
    private Status status;

    public void process_message_architecture_3(Message message) {
        byte[] diggested_message = this.digester.digest(message.getOlt().getBytes(StandardCharsets.UTF_8));
        int worker_to_forward = (ByteBuffer.wrap(diggested_message).getInt()) % status.getWorkers();
        if (worker_to_forward < 0 || worker_to_forward > status.getWorkers() - 1) {
            worker_to_forward = new Random().nextInt(status.getWorkers());
        }
        message.setWorker(worker_to_forward);
        Worker.forward_message(message, worker_to_forward, 3);
    }

    public void process_message_architecture_4(Message message) {
        int worker_to_forward = 0;
        if(status.getOracle().containsKey(message.getOlt())) {
            worker_to_forward = status.getOracle().get(message.getOlt());
            log.info("Found something on oracle so chose " + worker_to_forward);
        } else {
            worker_to_forward = (status.getLastChosenWorker() + 1) % status.getWorkers();
            log.info("Didn't find anything in the oracle so chose " + worker_to_forward);
        }
        log.info(converter.toJson(status.getOracle()));
        log.info("Chose worker " + worker_to_forward + " to forward");
        status.setLastChosenWorker(worker_to_forward);
        message.setWorker(worker_to_forward);
        Worker.forward_message(message, worker_to_forward, 4);
    }

    public void handleMessage(String body) {
        Message message = converter.fromJson(body, Message.class);
        log.info("Received message: " + converter.toJson(message));
        log.info("Current architecture: " + status.getArchitecture());
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
