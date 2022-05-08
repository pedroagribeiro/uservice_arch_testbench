package pt.testbench.olt.handlers;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pt.testbench.olt.communication.Worker;
import pt.testbench.olt.model.OltRequest;
import pt.testbench.olt.model.Response;
import pt.testbench.olt.model.Status;
import java.util.Date;

@Component
@Service
@Configurable
public class ReceiveMessageHandler {

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private static final Gson converter = new Gson();

    @Autowired private Status currentStatus;

    public void handleMessage(String body) {
        OltRequest request = converter.fromJson(body, OltRequest.class);
        request.setStartedBeingProcessedAtOlt(new Date().getTime());
        Response r = new Response(request.getId(), 200, new Date().getTime());
        r.setRequestEnqueuedAtOlt(currentStatus.getEnqueuedAtWorkerTimes().get(request.getId()));
        r.setRequestDequeuedAtOlt(new Date().getTime());
        log.info("Processing request: " + request.getId());
        try {
            Thread.sleep(request.getDuration());
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        request.setEndedBeingProcessedAtOlt(new Date().getTime());
        log.info("Finished processing request: " + request.getId());
        r.setEndedHandling(new Date().getTime());
        Worker.send_response(r, request.getOriginMessage().getWorker());
    }
}
