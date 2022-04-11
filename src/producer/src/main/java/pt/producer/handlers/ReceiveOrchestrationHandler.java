package pt.producer.handlers;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pt.producer.model.*;
import pt.producer.repository.ResultRepository;
import pt.producer.utils.Generator;

import java.util.*;

@Service
@Slf4j
public class ReceiveOrchestrationHandler {

    private final Gson converter = new Gson();
    private final Generator message_generator = new Generator();
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired private ResultRepository resultRepository;

    @Autowired
    @Qualifier("currentStatus")
    private Status current_status;

    private final String broker_host = "broker";
    private final String worker_base_host = "worker-";

    private void forward_orchestration_to_component(Orchestration orchestration, String host, int port) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Orchestration> entity = new HttpEntity<>(orchestration, headers);
        ResponseEntity<?> response = restTemplate.exchange("http://" + host + ":" + port + "/orchestration", HttpMethod.POST, entity, String.class);
        if(response.getStatusCode().isError()) {
            log.error("The message could not be sent to the component, something went wrong!");
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("Forwarded orchestration to the component!");
            }
        }
    }

    private void forward_orchestration_to_other_components(Orchestration orchestration, int workers) {
        // forward to the broker
        forward_orchestration_to_component(orchestration, this.broker_host, 8081);
        // forward to the workers
        for(int i = 0; i < workers; i++) {
            int port = 8500 + i;
            String worker_host = this.worker_base_host + i;
            forward_orchestration_to_component(orchestration, worker_host, port);
        }
    }

    private void inform_workers_of_target(int target, int workers) {
        for(int i = 0; i < workers; i++) {
            String worker_host = this.worker_base_host + i;
            int port = 8500 + i;
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<?> response = restTemplate.exchange("http://" + worker_host + ":" + port + "/run/target?target={target}", HttpMethod.POST, entity, String.class, target);
            if (response.getStatusCode().isError()) {
                log.info("Could not inform the workers of the run target, something went wrong!");
            } else {
                if (response.getStatusCode().is2xxSuccessful()) {
                    log.info("Informed the workers of the run target!");
                }
            }
        }
    }

    private void wait_for_current_run_to_finish() {
        while(this.current_status.isOnGoingRun()) {
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleOrchestration(String body) {
        wait_for_current_run_to_finish();
        Orchestration orchestration = this.converter.fromJson(body, Orchestration.class);
        inform_workers_of_target(this.current_status.getCurrentMessageId() + orchestration.getMessages() - 1, orchestration.getWorkers());
        forward_orchestration_to_other_components(orchestration, orchestration.getWorkers());
        this.current_status.start_run();
        int new_current_message_id = this.message_generator.generate_messages(this.current_status.getCurrentMessageId(), orchestration);
        log.info("Waiting for run results to be ready...");
        wait_for_current_run_to_finish();
        // calculate_run_results(orchestration.get_id(), current_status.getCurrentMessageId(), new_current_message_id - 1, new Date().getTime());
        log.info("The run is finished and the result has been submitted to the database");
        this.current_status.setCurrentMessageId(new_current_message_id + 1);
    }
}
