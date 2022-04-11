package pt.testbench.worker.handlers;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pt.testbench.worker.model.Message;
import pt.testbench.worker.model.OltRequest;
import pt.testbench.worker.model.Status;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import pt.testbench.worker.utils.SequenceGenerator;

@Service
@Slf4j
public class ReceiveMessageHandler {

    private final Gson converter = new Gson();
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private Status status;

    private String base_olt_host = "olt-";
    private String broker_host = "broker";

    private void perform_olt_request(OltRequest m, String olt) {
        String olt_host = base_olt_host;
        if(!olt_host.equals("localhost")) olt_host = olt_host + olt;
        int olt_port = 9000 + Integer.parseInt(olt);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<OltRequest> entity = new HttpEntity<>(m, headers);
        ResponseEntity<?> response = restTemplate.exchange("http://" + olt_host + ":" + olt_port + "/message", HttpMethod.POST, entity, String.class);
        if(response.getStatusCode().isError()) {
            log.info("The request could not be sent to the OLT, something went wrong!");
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("The request was sent to the olt!");
            }
        }
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

    private Callable<Boolean> request_satisfied(long request_id) {
        return () -> status.getRequestSatisfied().get(request_id);
    }

    public void handleMessage(String body) {
        Message m = converter.fromJson(body, Message.class);
        log.info("Received a message: " + converter.toJson(m));
        inform_oracle_of_handling(m.getOlt());
        List<OltRequest> generated_requests = SequenceGenerator.generate_requests_sequence(m);
        int timedout_requests = 0;
        for(int i = 0; i < generated_requests.size(); i++) {
            OltRequest request = generated_requests.get(i);
            status.setCurrentActiveRequest(request.getId());
            status.getRequestSatisfied().put(request.getId(), false);
            perform_olt_request(request, m.getOlt());
            try {
               Awaitility.await().atMost(request.getTimeout(), TimeUnit.MILLISECONDS).until(request_satisfied(request.getId()));
            } catch(Exception e) {
               log.warn("Timeout: The request " + request.getId() + " timeout");
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
    }
}
