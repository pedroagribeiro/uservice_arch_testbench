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
import pt.producer.repository.OltRequestRepository;
import pt.producer.repository.MessageRepository;
import pt.producer.utils.Generator;

import java.util.*;

@Service
@Slf4j
public class ReceiveOrchestrationHandler {

    @Autowired private ResultRepository resultRepository;
    @Autowired private OltRequestRepository oltRequestsRepository;
    @Autowired private MessageRepository messagesRepository;

    private final Gson converter = new Gson();
    private final Generator message_generator = new Generator();
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    @Qualifier("currentStatus")
    private Status current_status;

    private final String broker_host = "broker";
    private final String worker_base_host = "worker-";

    public void send_message_to_broker(Message m) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Message> entity = new HttpEntity<>(m, headers);
        ResponseEntity<?> response = restTemplate.exchange("http://broker:8081/message", HttpMethod.POST, entity, String.class);
        if(response.getStatusCode().isError()) {
            log.error("The message could not be sent to the broker, something went wrong!");
        } else {
            if(response.getStatusCode().is2xxSuccessful()) {
                log.info("Sent " + converter.toJson(m) + " to the broker.");
            }
        }
    }

    public void generate_messages(Orchestration orchestration) {
        int seed = 34;
        Random r = new Random(seed);
        List<Message> generated_messages = message_generator.generate_messages(orchestration);
        for(Message m : generated_messages) {
            m = this.messagesRepository.save(m);
            send_message_to_broker(m);
            int sleep_time = r.nextInt(10 + 5) * 100; 
            try {
                Thread.sleep(sleep_time);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

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

    /**
    private void calculate_run_results(int orchestration_id, int message_id_lower_boundary, int message_id_upper_boundary) {
        List<OltRequest> olt_requests = this.oltRequestsRepository.findRequestBetweenMessagesRange(message_id_lower_boundary, message_id_upper_boundary);
        List<Message> generated_messages = this.messagesRepository.findMessageBetweenIdRange(message_id_lower_boundary, message_id_upper_boundary);
        long minimum_theoretical_run_duration = 0;
        int mininum_theoretical_failed_provisions = 0;
        long verified_run_duration = 0;
        int verified_failed_provisions = 0;
        for(Message m : generated_messages) {
            minimum_theoretical_run_duration += m.getMinimumTheoreticalDuration();
            if(m.getHasRedRequests()) mininum_theoretical_failed_provisions += 1;
            if(m.getSuccessful() == false) verified_failed_provisions++; 
        }
        Optional<Result> run_result = this.resultRepository.findById(orchestration_id);
        if(run_result.isPresent()) {
            Result result = run_result.get();
            result.setStatus(Result.availableStatus[1]);
            result.setTheoreticalTotalTimeLimit(minimum_theoretical_run_duration);
            result.setTheoreticalTimeoutRequestsLimit(mininum_theoretical_failed_provisions);
            result.setVerifiedTotalTime(verified_run_duration);
            result.setVerifiedTimedoutRequests(verified_failed_provisions);
            this.resultRepository.save(result);
        }
    }
    **/

    public void handleOrchestration(String body) {
        wait_for_current_run_to_finish();
        Orchestration orchestration = this.converter.fromJson(body, Orchestration.class);
        int current_highest_message_id = 0;
        List<Message> list_of_highest_id_messages = this.messagesRepository.findMessageWithHighestId();
        if(list_of_highest_id_messages.size() != 0) {
            current_highest_message_id = list_of_highest_id_messages.get(0).getId();
        }
        inform_workers_of_target(current_highest_message_id + orchestration.getMessages() - 1, orchestration.getWorkers());
        forward_orchestration_to_other_components(orchestration, orchestration.getWorkers());
        this.current_status.start_run();
        generate_messages(orchestration);
        log.info("Waiting for run results to be ready...");
        wait_for_current_run_to_finish();
        // calculate_run_results(orchestration.getId(), current_status.getCurrentMessageId(), new_current_message_id - 1);
        log.info("The run is finished and the result has been submitted to the database");
    }
}
