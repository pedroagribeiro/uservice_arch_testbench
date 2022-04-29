package pt.testbench.worker.communication;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class Worker {

    private static final Logger log = LoggerFactory.getLogger("Worker");
    private static final Gson converter = new Gson();
    private static final RestTemplate restTemplate = new RestTemplate();

    public static final String host = "worker-%d";
    public static final int port = 8080;
    public static final Map<String, String> endpoints = new HashMap<>() {{
        put("inform_worker_run_is_over", "http://" + Worker.host + ":" + Worker.port + "/run/ended");
    }};
    
    public static void inform_workers_run_is_over(int workers) {
       for(int i = 0; i < workers; i++) {
           String url = String.format(Worker.endpoints.get("inform_worker_run_is_over"), i);
           HttpHeaders headers = new HttpHeaders();
           headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
           HttpEntity<?> entity = new HttpEntity<>(headers);
           ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
           if(response.getStatusCode().isError()) {
               log.info(String.format("Could not inform worker %d that the run is over. Something went wrong!", i));
           } else {
               if(response.getStatusCode().is2xxSuccessful()) {
                   log.info(String.format("Informed the worker %d that the run is over!", i));
               }
           }
       } 
    }
    
}
