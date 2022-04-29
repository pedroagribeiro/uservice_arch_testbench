package pt.testbench.broker.communication;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import pt.testbench.broker.model.Message;

public class Worker {
   
    private static final Logger log = LoggerFactory.getLogger("Worker");
    private static final Gson converter = new Gson();
    private static final RestTemplate restTemplate = new RestTemplate(); 

    public final static String host = "worker-%d";
    public final static int port = 8080;
    public final static Map<String, String> endpoints = new HashMap<>() {{
        put("message", "http://" + Worker.host + ":" + Worker.port + "/message");
    }};

    public static void forward_message(Message message, int worker, int logic) {
        String url = String.format(Worker.endpoints.get("message"), worker);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Message> entity = new HttpEntity<>(message, headers);
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        if(response.getStatusCode().isError()) {
            log.info(String.format("The message could not be forwarded to worker %d. Something went wrong!", worker));
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info(String.format("Forwarded message to worker %d using logic %d!", worker, logic));
            }
        }
    }


}
