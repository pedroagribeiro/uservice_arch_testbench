package pt.testbench.worker.communication;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import pt.testbench.worker.model.OltRequest;

public class Olt {

    private static final Logger log = LoggerFactory.getLogger("Olt");
    private static final Gson converter = new Gson();
    private static final RestTemplate restTemplate = new RestTemplate();

    public static final String host = "olt-%s";
    public static final int port = 8080;
    public static final Map<String, String> endpoints = new HashMap<>() {{
        put("message", "http://" + Olt.host + ":" + Olt.port + "/message");
    }};
   
    public static void perform_request(OltRequest request, String olt) {
        String url = String.format(Olt.endpoints.get("message"), olt);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<OltRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        if(response.getStatusCode().isError()) {
            log.info(String.format("The request could not be sent to OLT %s. Something went wrong!", olt));
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info(String.format("The request was sent to OLT %s", olt));
            }
        }
    }
}
