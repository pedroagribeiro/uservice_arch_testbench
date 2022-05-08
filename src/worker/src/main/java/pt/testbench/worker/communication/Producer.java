package pt.testbench.worker.communication;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import pt.testbench.worker.model.Message;
import pt.testbench.worker.model.OltRequest;
import pt.testbench.worker.model.Response;

public class Producer {

    private static final Logger log = LoggerFactory.getLogger("Producer");
    private static final Gson converter = new Gson();
    private static final RestTemplate restTemplate = new RestTemplate();
   
    public static final String host = "producer";
    public static final int port = 8080;
    public static final Map<String, String> endpoints = new HashMap<>() {{
        put("inform_producer_run_is_over", "http://" + Producer.host + ":" + Producer.port + "/run/ended?worker={worker}");
        put("send_run_messages", "http://" + Producer.host + ":" + Producer.port + "/run/messages?worker={worker}");
        put("send_run_requests", "http://" + Producer.host + ":" + Producer.port + "/run/requests?worker={worker}");
        put("send_run_responses", "http://" + Producer.host + ":" + Producer.port + "/run/responses?worker={worker}");
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

    public static void sendRunMessages(List<Message> run_messages, int worker) {
        String url = Producer.endpoints.get("send_run_messages");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<List<Message>> entity = new HttpEntity<>(run_messages, headers);
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class, worker);
        if(response.getStatusCode().isError()) {
            log.info("Couldn't send the messages of this run to the producer. Something went wrong!");
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("Sent the messages of this run to the producer.");
            }
        }
    }

    public static void sendRunRequests(List<OltRequest> run_requests, int worker) {
        String url = Producer.endpoints.get("send_run_requests");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<List<OltRequest>> entity = new HttpEntity<>(run_requests, headers);
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class, worker);
        if(response.getStatusCode().isError()) {
            log.info("Couldn't send the requests of this run to the producer. Something went wrong!");
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("Sent the requests of this run to the producer.");
            }
        }
    }

    public static void sendRunResponses(List<Response> run_responses, int worker) {
        String url = Producer.endpoints.get("send_run_responses");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<List<Response>> entity = new HttpEntity<>(run_responses, headers);
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class, worker);
        if(response.getStatusCode().isError()) {
            log.info("Couldn't send the responses of this run to the producer. Something went wrong!");
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("Sent the responses of this run to the producer.");
            }
        }
    }
}
