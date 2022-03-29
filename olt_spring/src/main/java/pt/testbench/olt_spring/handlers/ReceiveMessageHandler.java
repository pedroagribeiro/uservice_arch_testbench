package pt.testbench.olt_spring.handlers;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pt.testbench.olt_spring.model.Message;
import pt.testbench.olt_spring.model.Response;
import pt.testbench.olt_spring.repository.MessageRepository;

import java.util.Collections;
import java.util.Date;

@Service
@Slf4j
public class ReceiveMessageHandler {

    private static final Gson converter = new Gson();
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired private MessageRepository messageRepository;

    private void send_response_to_worker(Response r, String host, int worker) {
        int worker_port = 8500 + worker;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Response> entity = new HttpEntity<>(r, headers);
        String cenas = "http://" + host + ":" + worker_port + "/response";
        ResponseEntity<?> response = restTemplate.exchange(cenas, HttpMethod.POST, entity, String.class);
        if(response.getStatusCode().isError()) {
            log.info("The response could not be sent back to the worker, something went wrong!");
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("Sent the response back to the worker!");
            }
        }
    }

    public void handleMessage(String body) {
        Message m = converter.fromJson(body, Message.class);
        m.set_dequeued_at_olt(new Date().getTime());
        log.info("Received message: " + converter.toJson(m));
        log.info("Processing message " + m.get_id());
        Response r = new Response(200, new Date().getTime(), -1, m);
        // process the incoming message
        try {
            Thread.sleep(m.get_processing_time());
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        m.set_completed(new Date().getTime());
        log.info("Finished processing message " + m.get_id());
        r.set_ended_handling(new Date().getTime());
        send_response_to_worker(r, "localhost", m.get_worker());
    }
}
