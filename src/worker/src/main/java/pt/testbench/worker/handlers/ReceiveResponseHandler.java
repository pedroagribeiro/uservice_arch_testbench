package pt.testbench.worker.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pt.testbench.worker.communication.Broker;
import pt.testbench.worker.communication.Producer;
import pt.testbench.worker.communication.Worker;
import pt.testbench.worker.model.Message;
import pt.testbench.worker.model.OltRequest;
import pt.testbench.worker.model.Response;
import pt.testbench.worker.model.Status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
public class ReceiveResponseHandler {

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private final Gson converter = new Gson();

    @Autowired
    private Status status;

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

    public void handleResponse(String body) {
        Response r = converter.fromJson(body, Response.class);
        log.info("Response: " + converter.toJson(r));
        if(status.getCurrentRunRequests().containsKey(r.getId())) {
            OltRequest origin_request = status.getCurrentRunRequests().get(r.getId());
            origin_request.setStartedBeingProcessedAtOlt(r.getStartedHandling());
            origin_request.setEndedBeingProcessedAtOlt(r.getEndedHandling());
            origin_request.setReturnedWorker(new Date().getTime());
            origin_request.setCompleted(new Date().getTime());
            log.info("Got the following response: " + converter.toJson(r));
            status.getRequestSatisfied().put(r.getId(), true);
            // the default polling rate of Awaitility is 100 ms
            try {
                Thread.sleep(100);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            if(status.getCurrentActiveRequest().equals(r.getId())) {
                r.setTimedout(false);
                Broker.inform_oracle_of_handling_end(origin_request.getOriginMessage().getOlt());
                status.getCurrentRunResponses().put(r.getId(), r);
                this.status.getRequestSatisfied().remove(r.getId());
            } else {
                r.setTimedout(true);
                status.getCurrentRunResponses().put(r.getId(), r);
                this.status.getRequestSatisfied().remove(r.getId());
            }
            origin_request.setResponse(r);
            origin_request.setNotProcessed(false);
            status.getCurrentRunRequests().put(origin_request.getId(), origin_request);
            String[] split_id = r.getId().split("-");
            Message origin_message = status.getCurrentRunMessages().get(origin_request.getOriginMessage().getId());
            // Informar os restantes workers que o target foi atingido
            if((Integer.parseInt(split_id[0]) == status.getTargetMessageRun() && Integer.parseInt(split_id[1]) == 3) || (Integer.parseInt(split_id[0]) == status.getTargetMessageRun() && !origin_message.getSuccessful())) {
                Worker.inform_workers_target_has_been_reached(status.getWorkers());
            } 
            log.info("Target Reached: " + status.getTargetReached());
            log.info("Request to Satisfy size: " + status.getRequestSatisfied().size());
            log.info("Consumption Complete: " + status.getConsumptionComplete());
            if(status.getTargetReached() && status.getRequestSatisfied().size() == 0 && status.getConsumptionComplete() && (Integer.parseInt(split_id[1]) == 3 || !origin_message.getSuccessful())) {
                status.setIsOnGoingRun(false);
                Producer.inform_run_is_over(status.getWorkerId());
                Producer.sendRunResponses(convertCurrentRunResponseToList(), status.getWorkerId());
                Producer.sendRunMessages(convertCurrentRunMessagesToList(), status.getWorkerId());
                Producer.sendRunRequests(convertCurrentRunRequestsToList(), status.getWorkerId());
            }
            else {
                log.info("Message id: " + r.getId());
                log.info("Target: " + status.getTargetMessageRun());
                log.info("Requests to satisfy: " + converter.toJson(this.status.getRequestSatisfied()));
            }
        } else {
            log.info("FOR SOME REASON");
            log.info("Got a response for a request im not waiting! - " + r.getId());
        }
    }
}
