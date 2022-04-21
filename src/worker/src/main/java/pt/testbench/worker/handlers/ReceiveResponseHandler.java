package pt.testbench.worker.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pt.testbench.worker.model.OltRequest;
import pt.testbench.worker.model.Response;
import pt.testbench.worker.model.Status;
import pt.testbench.worker.repository.MessageRepository;
import pt.testbench.worker.repository.OltRequestRepository;
import pt.testbench.worker.repository.ResponseRepository;

import java.util.Collections;
import java.util.Date;

@Service
@Slf4j
public class ReceiveResponseHandler {

    private final Gson converter = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private RestTemplate restTemplate = new RestTemplate();

    @Autowired private MessageRepository messageRepository;
    @Autowired private OltRequestRepository oltRequestsRepository;
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

    private void inform_workers_run_is_over() {
        String worker_base_host = base_worker_host;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        for(int i = 0; i < status.getWorkers(); i++) {
            String host = base_worker_host + i;
            int port = 8500 + i;
            ResponseEntity<?> response = restTemplate.exchange("http://" + host + ":" + port + "/run/ended", HttpMethod.POST, entity, String.class);
            if(response.getStatusCode().isError()) {
                log.info("Could not inform worker " + i + " that the run is over, something went wrong!");
            } else {
                if(response.getStatusCode().is2xxSuccessful()) {
                    log.info("Informed the worker " + i + " that the run is over!");
                }
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
        OltRequest origin_request = this.oltRequestsRepository.findById(r.getId()).get();
        origin_request.setStartedBeingProcessedAtOlt(r.getStartedHandling());
        origin_request.setEndedBeingProcessedAtOlt(r.getEndedHandling());
        origin_request.setReturnedWorker(new Date().getTime());
        origin_request.setCompleted(new Date().getTime());
        log.info("Got the following response: " + converter.toJson(r));
        status.getRequestSatisfied().put(r.getId(), true);
        // the default polling rate of Awaitility is 100 ms
        try {
            Thread.sleep(100);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        if(status.getCurrentActiveRequest().equals(r.getId())) {
            r.setTimedout(false);
            inform_oracle_of_handling_end(origin_request.getOriginMessage().getOlt());
            r = this.responsesRepository.save(r);
            this.status.getRequestSatisfied().remove(r.getId());
        } else {
            r.setTimedout(true);
            r = this.responsesRepository.save(r);
            this.status.getRequestSatisfied().remove(r.getId());
        }
        origin_request.setResponse(r);
        origin_request.setNotProcessed(false);
        origin_request = this.oltRequestsRepository.save(origin_request);
        String[] split_id = r.getId().split("-");
        if(Integer.parseInt(split_id[0]) == status.getTargetMessageRun() && Integer.parseInt(split_id[1]) == 3 && this.status.getRequestSatisfied().size() == 0) {
            log.info("Run is over!!");
            status.setIsOnGoingRun(false);
            log.info("Informing workers the run is over");
            inform_workers_run_is_over();
            log.info("Informing the producer that the run is over");
            inform_producer_run_is_over();
        }
    }
}
