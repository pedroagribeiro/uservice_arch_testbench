package pt.producer.controller;

import java.util.List;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.producer.model.Message;
import pt.producer.model.OltRequest;
import pt.producer.model.Response;
import pt.producer.model.Status;
import pt.producer.repository.MessageRepository;
import pt.producer.repository.OltRequestRepository;
import pt.producer.repository.ResponseRepository;

@RestController
@RequestMapping("/run")
public class RunController {

    @Autowired private MessageRepository messagesRepository;
    @Autowired private OltRequestRepository oltsRequestsRepository;
    @Autowired private ResponseRepository responsesRepository;
    @Autowired private Status status;
    private Gson converter = new Gson();

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    
    private void is_run_finished() {
        boolean run_finished =
            status.getFinishedWorkers().size() == status.getCurrentRunWorkers() &&
            status.getSentMessagesWorkers().size() == status.getCurrentRunWorkers() &&
            status.getSentRequestsWorkers().size() == status.getCurrentRunWorkers() &&
            status.getSentResponsesWorkers().size() == status.getCurrentRunWorkers(); 
        if(run_finished) {
            status.end_run();
            log.info("Run is completely finished");
            log.info("All workers have finished. Updated the run staus to: " + status.isOnGoingRun());
        }
        log.info("Status: " + converter.toJson(status));
    }

    @PostMapping("/ended")
    public ResponseEntity<?> comunicateEndRun(@RequestParam int worker) {
        log.info("Worker " + worker + " told me he's finished");
        if(!status.getFinishedWorkers().contains(worker)) {
            status.getFinishedWorkers().add(worker);
        }
        is_run_finished();
        return new ResponseEntity<>("Worker " + worker + "has finished the run.", HttpStatus.OK);
    }

    @PostMapping("/messages")
    public ResponseEntity<?> communicateRunMessages(@RequestBody List<Message> run_messages, @RequestParam int worker) {
        log.info("Worker " + worker + " sent me the messages");
        if(!status.getSentMessagesWorkers().contains(worker)) {
            status.getSentMessagesWorkers().add(worker);
        };
        this.messagesRepository.saveAll(run_messages);
        is_run_finished();
        return new ResponseEntity<>("Worker " + worker + " has communicated the run_messages", HttpStatus.OK);
    }

    @PostMapping("/requests")
    public ResponseEntity<?> communicateRunRequests(@RequestBody List<OltRequest> run_requests, @RequestParam int worker) {
        log.info("Worker " + worker + " sent me the requests");
        if(!status.getSentRequestsWorkers().contains(worker)) {
            status.getSentRequestsWorkers().add(worker);
        };
        this.oltsRequestsRepository.saveAll(run_requests);
        is_run_finished();
        return new ResponseEntity<>("Worker " + worker + " has communicated the run_requests", HttpStatus.OK);
    }

    @PostMapping("/responses")
    public ResponseEntity<?> communicateRunResponses(@RequestBody List<Response> run_responses, @RequestParam int worker) {
        log.info("Worker " + worker + " sent me the responses");
        if(!status.getSentResponsesWorkers().contains(worker)) {
            status.getSentResponsesWorkers().add(worker);
        };
        this.responsesRepository.saveAll(run_responses);
        is_run_finished();
        return new ResponseEntity<>("Worker " + worker + " has communicated the run_responses", HttpStatus.OK);
    }
}
