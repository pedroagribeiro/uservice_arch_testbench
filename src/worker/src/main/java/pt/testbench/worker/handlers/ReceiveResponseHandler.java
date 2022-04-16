package pt.testbench.worker.handlers;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pt.testbench.worker.model.Response;
import pt.testbench.worker.model.Status;
import pt.testbench.worker.repository.MessageRepository;
import pt.testbench.worker.repository.ResponseRepository;

import java.util.Collections;

@Service
@Slf4j
public class ReceiveResponseHandler {

    private final Gson converter = new Gson();
    private RestTemplate restTemplate = new RestTemplate();

    @Autowired private MessageRepository messageRepository;
    @Autowired private ResponseRepository responsesRepository;

    @Autowired
    private Status status;

    private String producer_host = "producer";
    private String base_worker_host = "worker-";
    private String broker_host = "broker";

    private void inform_producer_run_is_over() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<?> response = restTemplate.exchange("http://" + producer_host + ":8080/run/ended", HttpMethod.POST, entity, String.class);
        if(response.getStatusCode().isError()) {
            log.info("Could not inform the producer that the run is over, something went wrong!");
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("Informed the producer that the run is over!");
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



    public void handleResponse(String body) {
        Response r = converter.fromJson(body, Response.class);
        status.getRequestSatisfied().put(r.getOriginRequest().getId(), true);
        if(status.getCurrentActiveRequest().equals(r.getOriginRequest().getId())) {
            r.setTimedout(false);
            inform_oracle_of_handling_end(r.getOriginRequest().getOriginMessage().getOlt());
            r = this.responsesRepository.save(r);
        } else {
            r.setTimedout(true);
            r = this.responsesRepository.save(r);
        }
        log.info("Received response to request " + r.getOriginRequest().getId() + ": " + converter.toJson(r));
        String[] split_id = r.getOriginRequest().getId().split("-");
        if(Integer.parseInt(split_id[0]) == status.getTargetMessageRun()) {
            status.setIsOnGoingRun(false);
            inform_producer_run_is_over();
        }
    }
}
