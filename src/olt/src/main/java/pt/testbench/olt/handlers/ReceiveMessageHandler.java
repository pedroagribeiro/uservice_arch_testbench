package pt.testbench.olt.handlers;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pt.testbench.olt.model.Message;
import pt.testbench.olt.model.OltRequest;
import pt.testbench.olt.model.Response;
import pt.testbench.olt.repository.OltRequestRepository;
import pt.testbench.olt.repository.ResponseRepository;

import java.util.Collections;
import java.util.Date;

@Service
@Slf4j
public class ReceiveMessageHandler {

    private static final Gson converter = new Gson();
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired private ResponseRepository responsesRepository;
    @Autowired private OltRequestRepository oltRequestsRepository;

    private String base_worker_host = "worker-";

    private void send_response_to_worker(Response r, int worker) {
        String worker_host = base_worker_host;
        if(!worker_host.equals("localhost")) worker_host = worker_host + worker;
        int worker_port = 8500 + worker;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Response> entity = new HttpEntity<>(r, headers);
        String cenas = "http://" + worker_host + ":" + worker_port + "/response";
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
        OltRequest m = converter.fromJson(body, OltRequest.class);
        m.setDequeuedAtOlt(new Date().getTime());
        log.info("Received request: " + converter.toJson(m));
        log.info("Processing request " + m.getId());
        Response r = new Response(m.getId() + "-response", 200, new Date().getTime());
        try {
            Thread.sleep(m.getDuration());
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Finished processing message " + m.getId());
        r.setEndedHandling(new Date().getTime());
        Response updated_response = this.responsesRepository.save(r);
        m.setResponse(updated_response);
        m = this.oltRequestsRepository.save(m);
        send_response_to_worker(updated_response, m.getOriginMessage().getWorker());
    }
}
