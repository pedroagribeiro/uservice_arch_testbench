package pt.testbench.worker.handlers;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import pt.testbench.worker.repository.OltRequestRepository;
import pt.testbench.worker.model.Message;
import pt.testbench.worker.model.OltRequest;
import pt.testbench.worker.model.Orchestration;
import pt.testbench.worker.model.Status;
import pt.testbench.worker.utils.SequenceGenerator;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ReceiveOrchestrationHandler {

    private static final Gson converter = new Gson();
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private Status status;

    private String broker_host = "broker";
    private String base_olt_host = "olt-";

   @Autowired
   private OltRequestRepository oltRequestRepository; 

    private Message fetch_message_from_broker() {
        Message m = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<?> response = restTemplate.exchange("http://" + broker_host + ":8081/message", HttpMethod.GET, entity, String.class);
            String message_json = (String) response.getBody();
            log.info("Actually fetched: " + message_json);
            m = converter.fromJson(message_json, Message.class);
            log.info("Fetched: " + converter.toJson(m) + " from the broker");
        } catch(HttpClientErrorException.NotFound e) {
           log.info("Could not fetch message from the broker, it is empty!");
        }
        return m;
    }

    private void inform_oracle_of_handling(String olt) {
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


    private void perform_olt_request(OltRequest m, String olt) {
        String olt_host = base_olt_host + olt;
        int olt_port = 9000 + Integer.parseInt(olt);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<OltRequest> entity = new HttpEntity<>(m, headers);
        ResponseEntity<?> response = restTemplate.exchange("http://" + olt_host + ":" + olt_port + "/message", HttpMethod.POST, entity, String.class);
        if(response.getStatusCode().isError()) {
            log.info("The message could not be sent to the OLT, something went wrong!");
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("The message was sent to the olt!");
            }
        }
    }

    private int ask_oracle_if_anyone_is_using_olt(String olt) {
        int broker_port = 8081;
        int worker = -1;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<?> response = restTemplate.exchange("http://" + broker_host + ":" + broker_port + "/management?olt={olt}", HttpMethod.GET, entity, String.class, olt);
            String worker_json = (String) response.getBody();
            worker = Integer.parseInt(worker_json);
        } catch(HttpClientErrorException.NotFound e) {
            log.info("There's no worker processing request for such olt");
        }
        return worker;
    }

    private Callable<Boolean> request_satisfied(long request_id) {
        return () -> status.getRequestSatisfied().get(request_id);
    }

    public void handleOrchestration(String body) {
        Orchestration orchestration = converter.fromJson(body, Orchestration.class);
        log.info("Received orchestration: " + converter.toJson(orchestration));
        status.setArchitecture(orchestration.getAlgorithm());
        log.info("Running logic " + status.getArchitecture() + " ...");
        status.setIsOnGoingRun(true);
        boolean auto_consume = status.isOnGoingRun() && (status.getArchitecture() == 1 || status.getArchitecture() == 2);
        if(auto_consume) {
            while(status.isOnGoingRun()) {
                log.info("Running logic: " + status.getArchitecture());
                log.info("Current target is: " + status.getTargetMessageRun());
                Message m = fetch_message_from_broker();
                if(m != null) {
                    long instant = new Date().getTime();
                    m.setWorker(status.getWorkerId());
                    if(status.getArchitecture() == 2) {
                        int worker = ask_oracle_if_anyone_is_using_olt(m.getOlt());
                        log.info("Oracle search result: " + worker);
                        while(worker != -1) {
                            try {
                                Thread.sleep(1000);
                            } catch(InterruptedException e) {
                                e.printStackTrace();
                            }
                            worker = ask_oracle_if_anyone_is_using_olt(m.getOlt());
                        }
                    }
                    log.info("Message that I got: " + converter.toJson(m));
                    List<OltRequest> generated_requests = SequenceGenerator.generate_requests_sequence(m);
                    inform_oracle_of_handling(m.getOlt());
                    int timedout_requests = 0;
                    for(int i = 0; i < generated_requests.size(); i++) {
                        OltRequest request = generated_requests.get(i);
                        // this.oltRequestRepository.save(request);
                        status.setCurrentActiveRequest(request.getId());
                        status.getRequestSatisfied().put(request.getId(), false);
                        perform_olt_request(request, m.getOlt());
                        try {
                           Awaitility.await().atMost(request.getTimeout(), TimeUnit.MILLISECONDS).until(request_satisfied(request.getId()));
                        } catch(Exception e) {
                           log.warn("Timeout: The request " + request.getId() + " timedout");
                           if(i == 3) {
                               inform_oracle_of_handling_end(m.getOlt());
                           }
                           m.setSuccessful(false);
                           timedout_requests++;
                        }
                    }
                    if(timedout_requests == 0) {
                        m.setSuccessful(true);
                    }
                } else {
                    try { 
                        Thread.sleep(500);
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
