package pt.testbench.worker.controller;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.testbench.worker.communication.Producer;
import pt.testbench.worker.model.Message;
import pt.testbench.worker.model.OltRequest;
import pt.testbench.worker.model.Response;
import pt.testbench.worker.model.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/run")
public class RunController {

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private Gson converter = new Gson();

    @Autowired
    private Status status;

    @Autowired
    AmqpAdmin amqpAdmin;

    private int queue_size() {
        log.info(converter.toJson(amqpAdmin.getQueueProperties("worker-" + status.getWorkerId() + "-message-queue")));
        return (Integer) amqpAdmin.getQueueProperties("worker-" + status.getWorkerId() + "-message-queue").get("QUEUE_MESSAGE_COUNT");
    }

    private List<Message> convertCurrentRunMessagesToList() {
        List<Message> run_messages = new ArrayList<>();
        Set<Integer> message_ids = status.getCurrentRunMessages().keySet();
        List<Integer> ordered_message_ids = new ArrayList<>(new TreeSet<>(message_ids));
        for(Integer i : ordered_message_ids) {
            run_messages.add(status.getCurrentRunMessages().get(i));
        }
        return run_messages;
    }

    private List<OltRequest> convertCurrentRunRequestsToList() {
        List<OltRequest> run_requests = new ArrayList<>();
        Set<String> request_ids = status.getCurrentRunRequests().keySet();
        List<String> ordered_request_ids = new ArrayList<>(new TreeSet<>(request_ids));
        for(String i : ordered_request_ids) {
            run_requests.add(status.getCurrentRunRequests().get(i));
        }
        return run_requests;
    }

    private List<Response> convertCurrentRunResponseToList() {
        List<Response> run_responses = new ArrayList<>();
        Set<String> response_ids = status.getCurrentRunResponses().keySet();
        List<String> ordered_response_ids = new ArrayList<>(new TreeSet<>(response_ids));
        for(String i : ordered_response_ids) {
            run_responses.add(status.getCurrentRunResponses().get(i));
        }
        return run_responses;
    }

    private void is_run_finished() {
        if(queue_size() == 0) {
            status.setComsumptionComplete(true);
        }
        if(status.getTargetReached() && status.getRequestSatisfied().size() == 0 && status.getConsumptionComplete()) {
            status.setIsOnGoingRun(false);
            Producer.inform_run_is_over(status.getWorkerId());
            Producer.sendRunResponses(convertCurrentRunResponseToList(), status.getWorkerId());
            Producer.sendRunMessages(convertCurrentRunMessagesToList(), status.getWorkerId());
            Producer.sendRunRequests(convertCurrentRunRequestsToList(), status.getWorkerId());
        }
    }

    @PostMapping("/started")
    public ResponseEntity<?> startedRun() {
        this.status.setIsOnGoingRun(true);
        return new ResponseEntity<>("Ongoing run: " + status.isOnGoingRun(), HttpStatus.OK);
    }

    @PostMapping("/target")
    public ResponseEntity<?> updateRunTarget(@RequestParam int target) {
        this.status.setTargetMessageRun(target);
        log.info("Updated run target to: " + target);
        return new ResponseEntity<>("Worker " + status.getWorkerId() +" has updated it's target to: " + target, HttpStatus.OK);
    }

    @PostMapping("/target_reached")
    public ResponseEntity<?> targetHasBeenReached() {
        this.status.setTargetReached(true);
        log.info("Got information that the target has been reached");
        log.info("Status: {on_going_run: " + this.status.isOnGoingRun() + ", target_reached: " + this.status.getTargetReached() + ", consumption_complete: " + this.status.getConsumptionComplete() + ", requests__size: " + this.status.getRequestSatisfied().size() + "}");
        is_run_finished();
        return new ResponseEntity<>("Target has been reached!", HttpStatus.OK);
    }
}
