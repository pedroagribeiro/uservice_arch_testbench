package pt.testbench.worker.handlers;

import com.google.gson.Gson;
import org.awaitility.Awaitility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.testbench.worker.communication.Broker;
import pt.testbench.worker.communication.Olt;
import pt.testbench.worker.model.Message;
import pt.testbench.worker.model.OltRequest;
import pt.testbench.worker.model.Orchestration;
import pt.testbench.worker.model.Status;
import pt.testbench.worker.utils.SequenceGenerator;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;


@Service
public class ReceiveOrchestrationHandler {

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private static final Gson converter = new Gson();

    @Autowired private Status status;

    private Callable<Boolean> request_satisfied(String request_id) {
        return () -> status.getRequestSatisfied().get(request_id);
    }

    private void update_status_with_orchestration(Orchestration orchestration) {
        status.setArchitecture(orchestration.getAlgorithm());
        status.setSequence(orchestration.getSequence());
        log.info("Running logic " + status.getArchitecture() + " ...");
        status.setIsOnGoingRun(true);
        status.setWorkers(orchestration.getWorkers());
        status.setTargetReached(false);
        status.setComsumptionComplete(false);
        status.getTimedoutProvisions().set(0);
        status.setCurrentRunMessages(new HashMap<>());
        status.setCurrentRunRequests(new HashMap<>());
        status.setCurrentRunResponses(new HashMap<>());
        status.setRequestSatisfied(new HashMap<>());
    }

    private void auto_consume_main_loop(Orchestration orchestration) {
        while(status.isOnGoingRun()) {
            // log.info("WHAT WORKER THINKS THE RUN STATUS IS: " + status.isOnGoingRun());
            Message m = Broker.fetch_message();
            if(m != null) {
                m.setWorker(status.getWorkerId());
                if(status.getArchitecture() == 2) {
                    int worker = Broker.ask_oracle_if_anyone_is_using_olt(m.getOlt());
                    log.info("Oracle search result: " + worker);
                    while(worker != -1) {
                        try {
                            Thread.sleep(1000);
                        } catch(InterruptedException e) {
                            e.printStackTrace();
                        }
                        worker = Broker.ask_oracle_if_anyone_is_using_olt(m.getOlt());
                    }
                }
                log.info("Message that I got: " + converter.toJson(m));
                List<OltRequest> generated_olt_requests = SequenceGenerator.generate_requests_sequence(m);
                status.getCurrentRunMessages().put(m.getId(), m);
                List<OltRequest> sorted_olt_requests = new ArrayList<>();
                assert generated_olt_requests != null;
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
                    if(!provision_timedout) {
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
                            Broker.inform_oracle_of_handling_end(m.getOlt());
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
            } else {
                // try { 
                //     Thread.sleep(500);
                // } catch(InterruptedException e) {
                //     e.printStackTrace();
                // }
                // log.info("Tried to consume message but there's nothing!");
                status.setComsumptionComplete(true);
            }
        }
    }

    public void handleOrchestration(String body) {
        Orchestration orchestration = converter.fromJson(body, Orchestration.class);
        log.info("Received orchestration: " + converter.toJson(orchestration));
        update_status_with_orchestration(orchestration);
        boolean auto_consume = status.isOnGoingRun() && (status.getArchitecture() == 1 || status.getArchitecture() == 2);
        if(auto_consume) {
            auto_consume_main_loop(orchestration);
        } 
    }
}
