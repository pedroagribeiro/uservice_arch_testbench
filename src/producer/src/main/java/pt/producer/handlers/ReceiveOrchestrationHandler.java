package pt.producer.handlers;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import pt.producer.communication.Broker;
import pt.producer.communication.Worker;
import pt.producer.model.*;
import pt.producer.repository.PerOltProcessingTimeRepository;
import pt.producer.repository.ResultRepository;
import pt.producer.repository.OltRequestRepository;
import pt.producer.repository.MessageRepository;
import pt.producer.utils.Generator;

import java.util.*;

@Service
public class ReceiveOrchestrationHandler {

    @Autowired private ResultRepository resultRepository;
    @Autowired private OltRequestRepository oltRequestsRepository;
    @Autowired private MessageRepository messagesRepository;
    @Autowired private PerOltProcessingTimeRepository perOltProcessingTimesRepository;

    private final Gson converter = new Gson();
    private final Generator message_generator = new Generator();

    @Autowired
    @Qualifier("currentStatus")
    private Status current_status;

    private Logger log = LoggerFactory.getLogger(this.getClass().getName()); 

    private void inform_workers_of_target(int target, int workers) {
        for(int i = 0; i < workers; i++) Worker.inform_of_target(target, i);
    }

    private void forward_orchestration_to_workers(Orchestration orchestration, int workers) {
        for(int i = 0; i < workers; i++) Worker.forward_orchestration(orchestration, i);
    }

    public void generate_and_send_messages(Orchestration orchestration) {
        List<Message> generated_messages = message_generator.generate_messages(orchestration);
        for(Message m : generated_messages) {
            m = this.messagesRepository.save(m);
            Broker.send_message(m);
        }
    }

    private void start_run(Orchestration orchestration) {
        Result r = this.resultRepository.findById(orchestration.getId()).get();
        r.setStatus(Result.availableStatus[1]);
        this.resultRepository.save(r);
        this.current_status.start_run();
        generate_and_send_messages(orchestration);
        log.info("Waiting for run results to be ready...");
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

    private void calculate_run_results(int orchestration_id, int message_id_lower_boundary, int message_id_upper_boundary) {
        long t_normal = 200;
        long end_instant = new Date().getTime();
        // List<OltRequest> olt_requests = this.oltRequestsRepository.findRequestsBetweenMessagesRange(message_id_lower_boundary, message_id_upper_boundary);
        List<Message> generated_messages = this.messagesRepository.findMessagesBetweenIdRange(message_id_lower_boundary, message_id_upper_boundary);
        int mininum_theoretical_failed_provisions = 0;
        int verified_failed_provisions = 0;
        for(Message m : generated_messages) {
            if(m.getHasRedRequests()) mininum_theoretical_failed_provisions += 1;
            if(!m.getSuccessful()) verified_failed_provisions++;
        }
        Optional<Result> run_result = this.resultRepository.findById(orchestration_id);
        if(run_result.isPresent()) {
            long verified_run_duration = end_instant - run_result.get().getStartInstant();
            long minimum_theoretical_run_duration = (run_result.get().getRequests() / run_result.get().getOlts()) * t_normal;
            Result result = run_result.get();
            result.setStatus(Result.availableStatus[1]);
            result.setEndInstant(end_instant);
            result.setTheoreticalTotalTimeLimit(minimum_theoretical_run_duration);
            result.setTheoreticalTimeoutRequestsLimit(mininum_theoretical_failed_provisions);
            result.setVerifiedTotalTime(verified_run_duration);
            result.setVerifiedTimedoutRequests(verified_failed_provisions);
            result.setStatus(Result.availableStatus[2]);
            result = this.resultRepository.save(result);
            // Calculating olt related measures
            Map<String, List<Long>> provisioning_times_by_olt = new HashMap<>();
            for(Message m : generated_messages) {
                if(m.getSuccessful()) {
                    long provisioning_time = m.getCompletedProcessing() - m.getStartedProcessing();
                    if (!provisioning_times_by_olt.containsKey(m.getOlt())) {
                        provisioning_times_by_olt.put(m.getOlt(), new ArrayList<>());
                    }
                    provisioning_times_by_olt.get(m.getOlt()).add(provisioning_time);
                }
            }
            for(String olt : provisioning_times_by_olt.keySet()) {
                List<Long> processing_times = provisioning_times_by_olt.get(olt);
                long minimum_processing_time = Collections.min(processing_times);
                long maximum_processing_time = Collections.max(processing_times);
                OptionalDouble average_processing_time_aux = processing_times.stream().mapToDouble(a -> a).average();
                double average_processing_time = average_processing_time_aux.isPresent() ? average_processing_time_aux.getAsDouble() : 0;
                PerOltProcessingTime save_registry = new PerOltProcessingTime(result, olt, minimum_processing_time, maximum_processing_time, average_processing_time);
                perOltProcessingTimesRepository.save(save_registry);
            }
        }
        log.info("The run is finished and the result has been submitted to the database");
    }

    public void update_run_state(int orchestration_id) {
        Result r = this.resultRepository.findById(orchestration_id).get();
        r.setStatus(Result.availableStatus[1]);
        this.resultRepository.save(r);
    }

    public void update_last_message_id() {
        int last_message_id = 0;
        List<Message> last_message = this.messagesRepository.findMessageWithHighestId();
        if(last_message.size() != 0) {
            last_message_id = last_message.get(0).getId();
        }
        current_status.setCurrentMessageId(last_message_id);
    }

    public void handleOrchestration(String body) {
        update_last_message_id();
        Orchestration orchestration = this.converter.fromJson(body, Orchestration.class);
        inform_workers_of_target(current_status.getCurrentMessageId() + orchestration.getMessages(), orchestration.getWorkers());
        Broker.forward_orchestration(orchestration);
        forward_orchestration_to_workers(orchestration, orchestration.getWorkers());
        start_run(orchestration);
        wait_for_current_run_to_finish();
        calculate_run_results(orchestration.getId(), current_status.getCurrentMessageId(), current_status.getCurrentMessageId() + orchestration.getMessages());
    }
}
