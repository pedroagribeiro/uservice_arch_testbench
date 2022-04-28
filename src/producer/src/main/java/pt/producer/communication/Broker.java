package pt.producer.communication;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import pt.producer.model.Message;
import pt.producer.model.Orchestration;

public class Broker {

    private static final Logger log = LoggerFactory.getLogger("Broker");
    private static final Gson converter = new Gson();
    private static final RestTemplate restTemplate = new RestTemplate();

    public final static String host ="broker";
    public final static int port = 8080;
    public final static Map<String, String> endpoints = new HashMap<String, String>() {{
        put("orchestration", "http://" + Broker.host + ":" + Broker.port + "/orchestration");
        put("message", "http://" + Broker.host + ":" + Broker.port + "/message");
    }};

    public static void forward_orchestration(Orchestration orchestration) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Orchestration> entity = new HttpEntity<>(orchestration, headers);
        ResponseEntity<?> response = restTemplate.exchange(Broker.endpoints.get("orchestration"), HttpMethod.POST, entity, String.class);
        if(response.getStatusCode().isError()) {
            log.error("The orchestration could not be forwarded to the broker. Something went wrong!");
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("Informed the broker of the orchestration!");
            }
        }
    } 

    public static void send_message(Message m) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Message> entity = new HttpEntity<>(m, headers);
        ResponseEntity<?> response = restTemplate.exchange(Broker.endpoints.get("message"), HttpMethod.POST, entity, String.class);
        if(response.getStatusCode().isError()) {
            log.error("The message could not be sent to the broker, something went wrong!");
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("Sent " + converter.toJson(m) + " to the broker.");
            }
        }
    }
    
}
