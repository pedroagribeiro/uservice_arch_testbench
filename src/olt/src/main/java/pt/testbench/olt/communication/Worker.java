package pt.testbench.olt.communication;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import pt.testbench.olt.model.Response;

public class Worker {

    private static final Logger log = LoggerFactory.getLogger("Worker");
    private static final RestTemplate restTemplate = new RestTemplate();

    public static final String host = "worker-%d";
    public static final int port = 8080;
    public static final Map<String, String> endpoints = new HashMap<>() {{
        put("send_response", "http://" + Worker.host + ":" + Worker.port + "/response");
    }};
   
    public static void send_response(Response r, int worker) {
        String url = String.format(Worker.endpoints.get("send_response"), worker);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Response> entity = new HttpEntity<>(r, headers);
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        if(response.getStatusCode().isError()) {
            log.info(String.format("The response could not be sent to worker %d. Something went wrong!", worker));
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info(String.format("Sent the response back to worker %d!", worker));
            }
        }
    }
}
