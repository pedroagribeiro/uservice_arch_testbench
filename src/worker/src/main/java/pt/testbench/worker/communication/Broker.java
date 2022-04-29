package pt.testbench.worker.communication;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pt.testbench.worker.model.Message;

public class Broker {

    private static final Logger log = LoggerFactory.getLogger("Broker");
    private static final Gson converter = new Gson();
    private static final RestTemplate restTemplate = new RestTemplate();

    public static final String host = "broker";
    public static final int port = 8080;
    public static final Map<String, String> endpoints = new HashMap<>() {{
        put("message", "http://" + Broker.host + ":" + Broker.port + "/message");
        put("inform_oracle_of_handling", "http://" + Broker.host + ":" + Broker.port + "/management?olt={olt}&worker={worker}");
        put("inform_oracle_of_handling_end", "http://" + Broker.host + ":" + Broker.port + "/management?olt={olt}");
        put("ask_oracle_if_anyone_is_using_olt", "http://" + Broker.host + ":" + Broker.port + "/management?olt={olt}");
    }};

    public static Message fetch_message() {
        String url = Broker.endpoints.get("message");
        Message m = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            if(response.getStatusCode().isError()) {
                log.info("Could not fetch message from the broker. Something went wrong!");
            } else {
                if(response.getStatusCode().is2xxSuccessful()) {
                    String message_json = (String) response.getBody();
                    m = converter.fromJson(message_json, Message.class);
                    log.info("Fetched " + converter.toJson(m) + " from the broker!");
                }
            }
        } catch(HttpClientErrorException.NotFound e) {
            log.info("Could not fetch message from the broker, it has no messages to provide!");
        }
        return m;
    }

    public static void inform_oracle_of_handling(String olt, int worker) {
        String url = Broker.endpoints.get("inform_oracle_of_handling");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class, olt, worker);
        if(response.getStatusCode().isError()) {
            log.info("Could not inform the broker of the handling. Something went wrong!");
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("Informed the broker of the handling!");
            }
        }
    }

    public static void inform_oracle_of_handling_end(String olt) {
        String url = Broker.endpoints.get("inform_oracle_of_handling_end");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class, olt);
        if(response.getStatusCode().isError()) {
            log.info("Could not inform the broker of the handling end. Something went wrong!");
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("Informed the broker of the handling end!");
            }
        }
    }
   
    public static int ask_oracle_if_anyone_is_using_olt(String olt) {
        String url = Broker.endpoints.get("ask_oracle_if_anyone_is_using_olt");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, olt); 
            String worker_json = (String) response.getBody();
            int worker = Integer.parseInt(worker_json);
            return worker;
        } catch(HttpClientErrorException.NotFound e) {
            log.info(String.format("No worker is processing requests for olt %s.", olt));
            return -1;
        }
    }
}
