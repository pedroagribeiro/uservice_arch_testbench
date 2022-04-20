package pt.testbench.worker.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pt.testbench.worker.model.Message;
import pt.testbench.worker.model.OltRequest;
import pt.testbench.worker.model.Status;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.awaitility.Awaitility;
import pt.testbench.worker.repository.MessageRepository;
import pt.testbench.worker.repository.OltRequestRepository;
import pt.testbench.worker.utils.SequenceGenerator;

@Service
@Slf4j
public class ReceiveMessageHandler {

    private final Gson converter = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private final RestTemplate restTemplate = new RestTemplate();

    private Random random = new Random(34);

    @Autowired private MessageRepository messagesRepository;
    @Autowired private OltRequestRepository oltRequestsRepository;

    @Autowired
    private Status status;

    private String base_olt_host = "olt-";
    private String broker_host = "broker";

    private void perform_olt_request(OltRequest m, String olt) {
        String olt_host = base_olt_host;
        if(!olt_host.equals("localhost")) olt_host = olt_host + olt;
        int olt_port = 9000 + Integer.parseInt(olt);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        m.setLeftWorker(new Date().getTime());
        log.info("Before saving: " + converter.toJson(m));
        this.oltRequestsRepository.save(m);
        log.info("After saving: " + converter.toJson(m));
        HttpEntity<OltRequest> entity = new HttpEntity<>(m, headers);
        ResponseEntity<?> response = restTemplate.exchange("http://" + olt_host + ":" + olt_port + "/message", HttpMethod.POST, entity, String.class);
        if(response.getStatusCode().isError()) {
            log.info("The request could not be sent to the OLT, something went wrong!");
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("The request was sent to the olt!");
            }
        }
    }

    private void inform_oracle_of_handling(String olt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<?> response = restTemplate.exchange("http://" + broker_host + ":8081/management?olt={olt}&worker={worker}", HttpMethod.POST, entity, String.class, olt, status.getWorkerId());
        if(response.getStatusCode().isError()) {
            log.info("Could not inform broker of the handling, something went wrong!");
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("Informed the broker of the handling!");
            }
        }
    }

    private void inform_oracle_of_handling_end(String olt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<?> response = restTemplate.exchange("http://" + broker_host + ":8081/management?olt={olt}", HttpMethod.DELETE, entity, String.class, olt);
        if(response.getStatusCode().isError()) {
            log.info("Could not inform broker of the handling end, something went wrong");
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("Informed the broker of the handling ending!");
            }
        }
    }

    private Callable<Boolean> request_satisfied(String request_id) {
        return () -> status.getRequestSatisfied().get(request_id);
    }

    public void handleMessage(String body) {
        Message m = converter.fromJson(body, Message.class);
        log.info("Received a message: " + converter.toJson(m));
        List<OltRequest> generated_olt_requests = null;
        if(status.getSequence() == 1) {
            generated_olt_requests = SequenceGenerator.generate_requests_sequence(m, -1, 0);
        }
        if(status.getSequence() == 2) {
            int left_yellow_messages = status.getTargetYellowMessages() - status.getGeneratedYellowMessages();
            int random_input = Math.min(left_yellow_messages, 4);
            int accessory_messages = random.nextInt(random_input);
            status.setGeneratedYellowMessages(status.getGeneratedYellowMessages() + accessory_messages);
            generated_olt_requests = SequenceGenerator.generate_requests_sequence(m, 0, accessory_messages);
        }
        if(status.getSequence() == 3) {
            int acessory_type = random.nextInt(2);
            if(acessory_type == 0) {
                int left_yellow_messages = status.getTargetYellowMessages() - status.getGeneratedYellowMessages();
                int random_input = Math.min(left_yellow_messages, 4);
                int accessory_messages = random.nextInt(random_input);
                status.setGeneratedYellowMessages(status.getGeneratedYellowMessages() + accessory_messages);
                generated_olt_requests = SequenceGenerator.generate_requests_sequence(m, 0, accessory_messages);
            } else {
                int left_red_messages = status.getTargetRedMessages() - status.getGeneratedRedMessages();
                int random_input = Math.min(left_red_messages, 4);
                int accessory_messages = random.nextInt(random_input);
                status.setGeneratedRedMessages(status.getGeneratedRedMessages() + accessory_messages);
                generated_olt_requests = SequenceGenerator.generate_requests_sequence(m, 1, accessory_messages);
            }
        }
        m = this.messagesRepository.save(m);
        List<OltRequest> sorted_olt_requests = new ArrayList<>();
        for(OltRequest request : generated_olt_requests) {
            request.setOriginMessage(m);
            request = this.oltRequestsRepository.save(request);
            sorted_olt_requests.add(request);
        }
        m.setStartedProcessing(new Date().getTime());
        m = this.messagesRepository.save(m);
        for(OltRequest request : sorted_olt_requests) {
            request.setOriginMessage(m);
        }
        inform_oracle_of_handling(m.getOlt());
        int timedout_requests = 0;
        boolean provision_timedout = false;
        for(int i = 0; i < sorted_olt_requests.size(); i++) {
            if (!provision_timedout) {
                OltRequest request = sorted_olt_requests.get(i);
                status.setCurrentActiveRequest(request.getId());
                status.getRequestSatisfied().put(request.getId(), false);
                request.setNotProcessed(false);
                perform_olt_request(request, m.getOlt());
                try {
                    Awaitility.await().atMost(request.getTimeout(), TimeUnit.MILLISECONDS).until(request_satisfied(request.getId()));
                } catch (Exception e) {
                    provision_timedout = true;
                    log.warn("Timeout: The request " + request.getId() + " timedout");
                    m.setSuccessful(false);
                    m = this.messagesRepository.save(m);
                    timedout_requests++;
                }
                if(i == 3) {
                    inform_oracle_of_handling_end(m.getOlt());
                }
            }
        }
        if(timedout_requests == 0) {
            m.setCompletedProcessing(new Date().getTime());
            m.setSuccessful(true);
            this.messagesRepository.save(m);
        }
    }
}
