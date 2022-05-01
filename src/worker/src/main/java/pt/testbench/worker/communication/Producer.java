package pt.testbench.worker.communication;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class Producer {

    private static final Logger log = LoggerFactory.getLogger("Producer");
    private static final Gson converter = new Gson();
    private static final RestTemplate restTemplate = new RestTemplate();
   
    public static final String host = "producer";
    public static final int port = 8080;
    public static final Map<String, String> endpoints = new HashMap<>() {{
        put("inform_producer_run_is_over", "http://" + Producer.host + ":" + Producer.port + "/run/ended?worker={worker}");
    }};

    public static void inform_run_is_over(int worker) {
        String url = Producer.endpoints.get("inform_producer_run_is_over");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class, worker);
        if(response.getStatusCode().isError()) {
            log.info("Could not inform the producer that the run is over. Something went wrong!");
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("Informed the producer that the run is over!");
            }
        }
    }
}
