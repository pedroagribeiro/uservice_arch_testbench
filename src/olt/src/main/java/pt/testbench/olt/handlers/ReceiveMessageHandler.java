package pt.testbench.olt.handlers;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pt.testbench.olt.communication.Worker;
import pt.testbench.olt.model.OltRequest;
import pt.testbench.olt.model.Response;
import pt.testbench.olt.model.Status;
import java.util.Date;

@Service
public class ReceiveMessageHandler {

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private static final Gson converter = new Gson();

    @Qualifier("createStatus")
    @Autowired private Status currentStatus;

    public void handleMessage(String body) {
        OltRequest request = converter.fromJson(body, OltRequest.class);
        request.setStartedBeingProcessedAtOlt(new Date().getTime());
        Response r = new Response(request.getId(), 200, new Date().getTime());
        r.setRequestEnqueuedAtOlt(currentStatus.getEnqueuedAtWorkerTimes().get(request.getId()));
        r.setRequestDequeuedAtOlt(new Date().getTime());
        log.info("Received request: " + converter.toJson(request));
        log.info("Processing request " + request.getId());
        try {
            Thread.sleep(request.getDuration());
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Finished processing message " + request.getId());
        request.setEndedBeingProcessedAtOlt(new Date().getTime());
        r.setEndedHandling(new Date().getTime());
        log.info("Received message to see if it has worker: " + converter.toJson(request));
        Worker.send_response(r, request.getOriginMessage().getWorker());
    }
}
