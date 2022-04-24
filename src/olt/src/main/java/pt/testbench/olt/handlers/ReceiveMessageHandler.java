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
import pt.testbench.olt.model.Status;

import java.util.Collections;
import java.util.Date;

@Slf4j
@Service
public class ReceiveMessageHandler {

    private static final Gson converter = new Gson();
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private Status currentStatus;

    private String base_worker_host = "worker-";

    private void send_response_to_worker(Response r, int worker) {
        String worker_host = base_worker_host;
        if(!worker_host.equals("localhost")) worker_host = worker_host + worker;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Response> entity = new HttpEntity<>(r, headers);
        String cenas = "http://" + worker_host + ":8080/response";
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
        OltRequest request = converter.fromJson(body, OltRequest.class);
        request.setStartedBeingProcessedAtOlt(new Date().getTime());
        Response r = new Response(request.getId(), 200, new Date().getTime());
        r.setRequestEnqueuedAtOlt(currentStatus.getEnqueuedAtWorkerTimes().get(request.getId()));
        r.setRequestDequeuedAtOlt(new Date().getTime());
        log.info("Received request: " + converter.toJson(request));
        log.info("Processing request " + request.getId());
        try {
            Thread.sleep(request.getDuration());
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Finished processing message " + request.getId());
        request.setEndedBeingProcessedAtOlt(new Date().getTime());
        r.setEndedHandling(new Date().getTime());
        log.info("Received message to see if it has worker: " + converter.toJson(request));
        send_response_to_worker(r, request.getOriginMessage().getWorker());
    }
}
