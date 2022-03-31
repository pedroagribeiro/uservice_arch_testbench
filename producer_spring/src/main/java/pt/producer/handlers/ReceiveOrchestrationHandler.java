package pt.producer.handlers;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pt.producer.model.*;
import pt.producer.repository.ResponseRepository;
import pt.producer.repository.ResultRepository;
import pt.producer.utils.Generator;

import java.util.*;

@Service
@Slf4j
public class ReceiveOrchestrationHandler {

    private final Gson converter = new Gson();
    private final Generator message_generator = new Generator();
    private RestTemplate restTemplate = new RestTemplate();

    @Autowired private ResponseRepository responseRepository;

    @Autowired private ResultRepository resultRepository;

    @Autowired
    @Qualifier("currentStatus")
    private Status current_status;

    private String broker_host = "broker";
    private String worker_base_host = "worker-";

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
            if(this.worker_base_host.equals("localhost")) {
                forward_orchestration_to_component(orchestration, "localhost", port);
            } else {
                String worker_host = this.worker_base_host + i;
                forward_orchestration_to_component(orchestration, worker_host, port);
            }
        }
    }

    private void calculate_run_results(int run_id, int lower_bound, int upper_bound) {
        int total_requests = upper_bound - lower_bound;
        List<Response> responses = this.responseRepository.findAllResponsesBetweenGivenIds(lower_bound, upper_bound);
        List<RequestReport> reports = new ArrayList<>();
        for(Response r : responses) {
            int request_id = r.get_origin_message().get_id();
            String olt = r.get_origin_message().get_olt();
            long total_time = r.get_origin_message().get_completed() - r.get_origin_message().get_issued_at();
            long time_broker_queue = r.get_origin_message().get_dequeued_at_broker() - r.get_origin_message().get_enqueued_at_broker();
            long time_worker_queue = r.get_origin_message().get_dequeued_at_worker() - r.get_origin_message().get_enqueued_at_worker();
            long time_olt_queue = r.get_origin_message().get_dequeued_at_olt() - r.get_origin_message().get_enqueued_at_olt();
            boolean timedout = r.get_timedout();
            RequestReport report = new RequestReport(request_id, olt, total_time, time_broker_queue, time_worker_queue, time_olt_queue, timedout);
            reports.add(report);
        }
        long total_time_total = 0;
        long time_broker_queue_total = 0;
        long time_worker_queue_total = 0;
        long time_olt_queue_total = 0;
        long timedout_requests = 0;
        for(RequestReport r : reports) {
            total_time_total += r.get_total_time();
            time_broker_queue_total += r.get_time_broker_queue();
            time_worker_queue_total += r.get_time_worker_queue();
            time_olt_queue_total += r.get_time_olt_queue();
            if(r.get_timedout()) timedout_requests = timedout_requests + (long) 1;
        }
        double avg_time_total = total_time_total / total_requests;
        double avg_time_broker_queue = time_broker_queue_total / total_requests;
        double avg_time_worker_queue = time_worker_queue_total / total_requests;
        double avg_time_olt_queue = time_olt_queue_total / total_requests;
        double percentage_of_timedout_requests = (double) timedout_requests / (double) total_requests;
        Optional<Result> r = this.resultRepository.findById(run_id);
        if(r.isPresent()) {
            Result result = r.get();
            result.setAvg_time_total(avg_time_total);
            result.setAvg_time_broker_queue(avg_time_broker_queue);
            result.setAvg_time_worker_queue(avg_time_worker_queue);
            result.setAvg_time_olt_queue(avg_time_olt_queue);
            result.setTimedout(percentage_of_timedout_requests);
            result.setStatus("FINISHED");
            this.resultRepository.save(result);
        }
    }

    private void inform_workers_of_target(int target, int workers) {
        for(int i = 0; i < workers; i++) {
            String worker_host = "localhost";
            if(!this.worker_base_host.equals("localhost")) {
                worker_host = this.worker_base_host + i;
            }
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

    public void handleOrchestration(String body) {
        while(this.current_status.isOnGoingRun()) {
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        Orchestration orchestration = this.converter.fromJson(body, Orchestration.class);
        inform_workers_of_target(current_status.getCurrentMessageId() + orchestration.get_messages() - 1, orchestration.get_workers());
        forward_orchestration_to_other_components(orchestration, orchestration.get_workers());
        this.current_status.start_run();
        int new_current_message_id = this.message_generator.generate_messages(current_status.getCurrentMessageId(), orchestration);
        log.info("Waiting for run results to be ready...");
        while(current_status.isOnGoingRun()) {}
        calculate_run_results(orchestration.get_id(), current_status.getCurrentMessageId(), new_current_message_id - 1);
        log.info("The run is finished and the result has been submitted to the database");
        this.current_status.setCurrentMessageId(new_current_message_id + 1);
    }
}
