package pt.testbench.worker_spring.handlers;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pt.testbench.worker_spring.model.Message;
import pt.testbench.worker_spring.model.Status;

import java.util.Collections;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;

@Service
@Slf4j
public class ReceiveMessageHandler {

    private final Gson converter = new Gson();
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private Status status;

    @Value("${spring.base_olt.host}")
    private String base_olt_host;

    @Value("{spring.broker.host}")
    private String broker_host;

    private void perform_olt_request(Message m, String olt) {
        String olt_host = base_olt_host;
        if(!olt_host.equals("localhost")) olt_host = olt_host + olt;
        int olt_port = 9000 + Integer.parseInt(olt);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Message> entity = new HttpEntity<>(m, headers);
        ResponseEntity<?> response = restTemplate.exchange("http://" + olt_host + ":" + olt_port + "/message", HttpMethod.POST, entity, String.class);
        if(response.getStatusCode().isError()) {
            log.info("The message could not be sent to the OLT, something went wrong!");
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("The message was sent to the olt!");
            }
        }
    }

    private void inform_oracle_of_handling(String olt) {
        if(!broker_host.equals("localhost")) broker_host = broker_host + olt;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<?> response = restTemplate.exchange("http://" + broker_host + ":8081/management?olt={olt}&worker={worker}", HttpMethod.POST, entity, String.class, olt, status.getWorkerId());
        if(response.getStatusCode().isError()) {
            log.info("Could not inform broker of the handling, something went wrong!");
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("Informed the broker of the handling!");
            }
        }
    }

    private void inform_oracle_of_handling_end(String olt) {
        if(!broker_host.equals("localhost")) broker_host = broker_host + olt;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<?> response = restTemplate.exchange("http://" + broker_host + ":8081/management?olt={olt}", HttpMethod.DELETE, entity, String.class, olt);
        if(response.getStatusCode().isError()) {
            log.info("Could not inform broker of the handling end, something went wrong");
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("Informed the broker of the handling ending!");
            }
        }
    }

    private Callable<Boolean> request_satisfied(int request_id) {
        return () -> status.getRequestSatisfied().get(request_id);
    }

    public void handleMessage(String body) {
        Message m = converter.fromJson(body, Message.class);
        m.set_dequeued_at_worker(new Date().getTime());
        log.info("Received a message: " + converter.toJson(m));
        inform_oracle_of_handling(m.get_olt());
        status.setCurrentActiveRequest(m.get_id());
        status.getRequestSatisfied().put(m.get_id(), false);
        perform_olt_request(m, m.get_olt());
        try {
            Awaitility.await().atMost(m.get_timeout(), TimeUnit.MILLISECONDS).until(request_satisfied(m.get_id()));
        } catch(Exception e) {
            log.warn("Timeout: The request " + m.get_id() + " timedout");
            inform_oracle_of_handling_end(m.get_olt());
        }
    }
}
