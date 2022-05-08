package pt.testbench.worker.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.testbench.worker.communication.Broker;
import pt.testbench.worker.communication.Olt;
import pt.testbench.worker.model.Message;
import pt.testbench.worker.model.OltRequest;
import pt.testbench.worker.model.Status;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import org.awaitility.Awaitility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.testbench.worker.utils.SequenceGenerator;

@Service
@Slf4j
public class ReceiveMessageHandler {

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private final Gson converter = new Gson();

    @Autowired private Status status;
    @Autowired AmqpAdmin amqpAdmin;

    private Callable<Boolean> request_satisfied(String request_id) {
        return () -> status.getRequestSatisfied().get(request_id);
    }

    private int queue_size() {
        log.info(converter.toJson(amqpAdmin.getQueueProperties("worker-" + status.getWorkerId() + "-message-queue")));
        return (Integer) amqpAdmin.getQueueProperties("worker-" + status.getWorkerId() + "-message-queue").get("QUEUE_MESSAGE_COUNT");
    }

    public void handleMessage(String body) {
        Message m = converter.fromJson(body, Message.class);
        log.info("Received a message: " + converter.toJson(m));
        List<OltRequest> generated_olt_requests = SequenceGenerator.generate_requests_sequence(m);
        status.getCurrentRunMessages().put(m.getId(), m);
        List<OltRequest> sorted_olt_requests = new ArrayList<>();
        for(OltRequest request : generated_olt_requests) {
            request.setOriginMessage(m);
            status.getCurrentRunRequests().put(request.getId(), request);
            sorted_olt_requests.add(request);
        }
        m.setStartedProcessing(new Date().getTime());
        status.getCurrentRunMessages().put(m.getId(), m);
        for(OltRequest request : sorted_olt_requests) {
            request.setOriginMessage(m);
        }
        Broker.inform_oracle_of_handling(m.getOlt(), status.getWorkerId());
        int timedout_requests = 0;
        boolean provision_timedout = false;
        for(int i = 0; i < sorted_olt_requests.size(); i++) {
            if (!provision_timedout) {
                OltRequest request = sorted_olt_requests.get(i);
                status.setCurrentActiveRequest(request.getId());
                status.getRequestSatisfied().put(request.getId(), false);
                request.setNotProcessed(false);
                request.setLeftWorker(new Date().getTime());
                // request = this.oltRequestsRepository.save(request);
                Olt.perform_request(request, m.getOlt());
                try {
                    Awaitility.await().atMost(request.getTimeout(), TimeUnit.MILLISECONDS).until(request_satisfied(request.getId()));
                } catch (Exception e) {
                    provision_timedout = true;
                    log.warn("Timeout: The request " + request.getId() + " timedout");
                    m.setCompletedProcessing(new Date().getTime());
                    m.setSuccessful(false);
                    status.getCurrentRunMessages().put(m.getId(), m);
                    timedout_requests++;
                    status.getTimedoutProvisions().set(status.getTimedoutProvisions().get() + 1);
                }
                if(i == 3) {
                    Broker.inform_oracle_of_handling_end(m.getOlt());
                }
            }
            log.info("Timedout provisions: " + status.getTimedoutProvisions().get());
        }
        if(timedout_requests == 0) {
            m.setCompletedProcessing(new Date().getTime());
            m.setSuccessful(true);
            status.getCurrentRunMessages().put(m.getId(), m);
        }
        log.info("Queue size: " + queue_size());
        if(queue_size() == 0) {
            status.setComsumptionComplete(true);
        }
        log.info("Queue size: " + queue_size());
    }
}
