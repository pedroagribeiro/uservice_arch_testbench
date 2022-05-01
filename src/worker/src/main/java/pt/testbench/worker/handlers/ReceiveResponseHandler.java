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
import pt.testbench.worker.repository.MessageRepository;
import pt.testbench.worker.repository.OltRequestRepository;
import pt.testbench.worker.repository.ResponseRepository;
import java.util.Collections;
import java.util.Date;

@Service
public class ReceiveResponseHandler {

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private final Gson converter = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    @Autowired private MessageRepository messageRepository;
    @Autowired private OltRequestRepository oltRequestsRepository;
    @Autowired private ResponseRepository responsesRepository;

    @Autowired
    private Status status;

    public void handleResponse(String body) {
        Response r = converter.fromJson(body, Response.class);
        OltRequest origin_request = this.oltRequestsRepository.findById(r.getId()).get();
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
            r = this.responsesRepository.save(r);
            this.status.getRequestSatisfied().remove(r.getId());
        } else {
            r.setTimedout(true);
            r = this.responsesRepository.save(r);
            this.status.getRequestSatisfied().remove(r.getId());
        }
        origin_request.setResponse(r);
        origin_request.setNotProcessed(false);
        origin_request = this.oltRequestsRepository.save(origin_request);
        String[] split_id = r.getId().split("-");
        Message origin_message = this.messageRepository.findById(origin_request.getOriginMessage().getId()).get();
        if((Integer.parseInt(split_id[0]) == status.getTargetMessageRun() && Integer.parseInt(split_id[1]) == 3) || (Integer.parseInt(split_id[0]) == status.getTargetMessageRun() && !origin_message.getSuccessful())) {
            log.info("Finished processing target message");
            status.setIsOnGoingRun(false);
            log.info("Informing workers the target has been reached");
            Worker.inform_workers_target_has_been_reached(status.getWorkers());
        } 
        log.info("Target Reached: " + status.getTargetReached());
        log.info("Request to Satisfy size: " + status.getRequestSatisfied().size());
        log.info("Consumption Complete: " + status.getConsumptionComplete());
        if(status.getTargetReached() && status.getRequestSatisfied().size() == 0 && status.getConsumptionComplete()) {
            log.info("Informing the producer that the run is over");
            Producer.inform_run_is_over(status.getWorkerId());
        }
        else {
            log.info("Message id: " + r.getId());
            log.info("Target: " + status.getTargetMessageRun());
            // log.info("Requests to satisfy: " + this.status.getRequestSatisfied().size());
            log.info("Requests to satisfy: " + converter.toJson(this.status.getRequestSatisfied()));
        }
        log.info("Target yellow messages: " + status.getTargetYellowMessages());
        log.info("Generated yellow messages: " + status.getGeneratedYellowMessages());
        log.info("Target red messages: " + status.getTargetRedMessages());
        log.info("Generated red messages: " + status.getGeneratedRedMessages());
    }
}
