package pt.producer.handlers;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pt.producer.communication.Broker;
import pt.producer.communication.Worker;
import pt.producer.model.*;
import pt.producer.repository.PerOltProcessingTimeRepository;
import pt.producer.repository.ResultRepository;
import pt.producer.repository.MessageRepository;
import pt.producer.utils.Generator;
import pt.producer.utils.ResultCalculator;

import java.io.IOError;
import java.util.*;

@Service
public class ReceiveOrchestrationHandler {

    @Autowired private ResultRepository resultRepository;
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
        r.setStartInstant(new Date().getTime());
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
        long end_instant = new Date().getTime();
        List<Message> run_messages = this.messagesRepository.findMessagesBetweenIdRange(message_id_lower_boundary, message_id_upper_boundary);
        Result run_result = this.resultRepository.findById(orchestration_id).get();
        run_result = ResultCalculator.calculate_run_result(end_instant, run_messages, run_result);
        run_result = this.resultRepository.save(run_result);
        List<PerOltProcessingTime> registries = ResultCalculator.calculate_per_olt_metrics(run_messages, run_result);
        for(PerOltProcessingTime registry : registries) this.perOltProcessingTimesRepository.save(registry);
        log.info("The run is finished and the result has been submitted to the database");
    }

    public void update_run_state(int orchestration_id) {
        Result r = this.resultRepository.findById(orchestration_id).get();
        r.setStatus(Result.availableStatus[1]);
        this.resultRepository.save(r);
    }

    public void update_last_message_id() {
        int last_message_id = 1;
        List<Message> last_message = this.messagesRepository.findMessageWithHighestId();
        if(last_message.size() != 0) {
            last_message_id = last_message.get(0).getId() + 1;
        }
        current_status.setCurrentMessageId(last_message_id);
    }

    public void update_status_with_orchestration(Orchestration orchestration) {
        current_status.setCurrentRunWorkers(orchestration.getWorkers());
        current_status.setFinishedWorkers(new ArrayList<>());
    }

    public void handleOrchestration(String body) {
        update_last_message_id();
        Orchestration orchestration = this.converter.fromJson(body, Orchestration.class);
        update_status_with_orchestration(orchestration);
        inform_workers_of_target(current_status.getCurrentMessageId() + orchestration.getMessages() - 1, orchestration.getWorkers());
        Broker.forward_orchestration(orchestration);
        forward_orchestration_to_workers(orchestration, orchestration.getWorkers());
        start_run(orchestration);
        wait_for_current_run_to_finish();
        calculate_run_results(orchestration.getId(), current_status.getCurrentMessageId(), current_status.getCurrentMessageId() + orchestration.getMessages());
    }
}
