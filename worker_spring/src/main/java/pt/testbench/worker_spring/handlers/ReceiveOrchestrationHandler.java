package pt.testbench.worker_spring.handlers;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pt.testbench.worker_spring.model.Message;
import pt.testbench.worker_spring.model.Orchestration;
import pt.testbench.worker_spring.model.Status;

import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ReceiveOrchestrationHandler {

    private static final Gson converter = new Gson();
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private Status status;

    private Message fetch_message_from_broker(String host) {
        Message m = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<?> response = restTemplate.exchange("http://" + host + ":8081/message", HttpMethod.GET, entity, String.class);
            String message_json = (String) response.getBody();
            log.info("Actually fetched: " + message_json);
            m = converter.fromJson(message_json, Message.class);
            log.info("Fetched: " + converter.toJson(m) + " from the broker");
        } catch(HttpClientErrorException.NotFound e) {
           log.info("Could not fetch message from the broker, it is empty!");
        }
        return m;
    }


    private void perform_olt_request(Message m, String host, String olt) {
        int olt_port = 9000 + Integer.parseInt(olt);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Message> entity = new HttpEntity<>(m, headers);
        ResponseEntity<?> response = restTemplate.exchange("http://" + host + ":" + olt_port + "/message", HttpMethod.POST, entity, String.class);
        if(response.getStatusCode().isError()) {
            log.info("The message could not be sent to the OLT, something went wrong!");
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("The message was sent to the olt!");
            }
        }
    }

    private int ask_oracle_if_anyone_is_using_olt(String host, String olt) {
        int broker_port = 8081;
        int worker = -1;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<?> response = restTemplate.exchange("http://" + host + ":" + broker_port + "/management?olt={olt}", HttpMethod.GET, entity, String.class, olt);
            String worker_json = (String) response.getBody();
            worker = Integer.parseInt(worker_json);
        } catch(HttpClientErrorException.NotFound e) {
            log.info("There's no worker processing request for such olt");
        }
        return worker;
    }

    private Callable<Boolean> request_satisfied(int request_id) {
        return () -> status.getRequestSatisfied().get(request_id);
    }

    public void handleOrchestration(String body) {
        Orchestration orchestration = converter.fromJson(body, Orchestration.class);
        log.info("Received orchestration: " + converter.toJson(orchestration));
        status.setArchitecture(orchestration.get_algorithm());
        log.info("Running logic " + status.getArchitecture() + " ...");
        status.setIsOnGoingRun(true);
        boolean auto_consume = status.isOnGoingRun() && (status.getArchitecture() == 1 || status.getArchitecture() == 2);
        if(auto_consume) {
            while(status.isOnGoingRun()) {
                Message m = fetch_message_from_broker("localhost");
                if(status.getArchitecture() == 2) {
                    int worker = ask_oracle_if_anyone_is_using_olt("localhost", m.get_olt());
                    while(worker != -1) {
                       try {
                           Thread.sleep(1000);
                       } catch(InterruptedException e) {
                           e.printStackTrace();
                       }
                       worker = ask_oracle_if_anyone_is_using_olt("localhost", m.get_olt());
                    }
                }
                if(m != null) {
                    status.setCurrentActiveRequest(m.get_id());
                    status.getRequestSatisfied().put(m.get_id(), false);
                    log.info("Message that I got: " + converter.toJson(m));
                    perform_olt_request(m, "localhost", m.get_olt());
                    try {
                        Awaitility.await().atMost(m.get_timeout(), TimeUnit.MILLISECONDS).until(request_satisfied(m.get_id()));
                    } catch(Exception e) {
                        log.warn("Timeout: The request " + m.get_id() + " timedout");
                    }
                }
                try {
                    Thread.sleep(2000);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
