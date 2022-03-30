package pt.testbench.worker_spring.handlers;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pt.testbench.worker_spring.model.Response;
import pt.testbench.worker_spring.model.Status;
import pt.testbench.worker_spring.repository.ResponseRepository;

import java.util.Collections;

@Service
@Slf4j
public class ReceiveResponseHandler {

    private final Gson converter = new Gson();
    private RestTemplate restTemplate = new RestTemplate();

    @Autowired private ResponseRepository responseRepository;

    @Autowired
    private Status status;

    @Value("${spring.producer.host}")
    private String producer_host;

    @Value("${spring.base_worker.host}")
    private String base_worker_host;

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

    private void inform_workers_run_is_over(String host) {
        String worker_base_host = base_worker_host;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<?> response = restTemplate.exchange("http://" + host + ":8080/run/ended", HttpMethod.POST, entity, String.class);
        if(response.getStatusCode().isError()) {
            log.info("Could not inform the producer that the run is over, something went wrong!");
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("Informed the producer that the run is over!");
            }
        }
    }


    public void handleResponse(String body) {
        Response r = converter.fromJson(body, Response.class);
        status.getRequestSatisfied().put(r.get_origin_message().get_id(), true);
        if(status.getCurrentActiveRequest() == r.get_origin_message().get_id()) {
            r.set_timedout(false);
        } else {
            r.set_timedout(true);
        }
        responseRepository.save(r);
        log.info("Received response to request " + r.get_origin_message().get_id() + ": " + converter.toJson(r));
        if(r.get_origin_message().get_id() == status.getTargetMessageRun()) {
            status.setIsOnGoingRun(false);
            inform_producer_run_is_over();
            // tell the other workers the run is over
            // tell the broker it is over
            // tell the producer it is over
        }
    }
}
