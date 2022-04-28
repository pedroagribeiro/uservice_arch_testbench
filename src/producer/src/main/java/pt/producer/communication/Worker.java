package pt.producer.communication;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import pt.producer.model.Orchestration;

public class Worker {

    private static final Logger log = LoggerFactory.getLogger("Worker");
    private static final Gson converter = new Gson();
    private static final RestTemplate restTemplate = new RestTemplate();

    public final static String host = "worker-%s";
    public final static int port = 8080;
    public final static Map<String, String> endpoints = new HashMap<>() {{
        put("orchestration", "http://" + Worker.host + ":" + Worker.port + "/orchestration");
        put("target", "http://" + Worker.host + ":" + Worker.port + "/target?target={target}");
    }}; 

    public static void forward_orchestration(Orchestration orchestration, int worker) {
        String url = String.format(Worker.endpoints.get("orchestration"), worker);
        log.info("Worker url: " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Orchestration> entity = new HttpEntity<>(orchestration, headers);
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        if(response.getStatusCode().isError()) {
            log.error(String.format("The orchestration could not be forwarded to the worker %. Something went wrong!", worker));
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info(String.format("Informed the worker % of the orchestration!"));
            }
        }
    }
    

    public static void inform_of_target(int target, int worker) {
        String url = String.format(Worker.endpoints.get("target"), worker); 
           HttpHeaders headers = new HttpHeaders();
           headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
           HttpEntity<?> entity = new HttpEntity<>(headers);
           ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class, target);
           if(response.getStatusCode().isError()) {
               log.info(String.format("Could not inform the worker %s of the run target. Something went wrong!", worker));
           } else {
               if(response.getStatusCode().is2xxSuccessful()) {
                   log.info(String.format("Informed the worker %s of the run target!"));
               }
           }
    }
}
