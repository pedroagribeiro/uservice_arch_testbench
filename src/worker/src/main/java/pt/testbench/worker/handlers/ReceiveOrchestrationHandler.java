package pt.testbench.worker.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
import pt.testbench.worker.repository.MessageRepository;
import pt.testbench.worker.repository.OltRequestRepository;
import pt.testbench.worker.utils.SequenceGenerator;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Service
public class ReceiveOrchestrationHandler {

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private static final Gson converter = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    private Random random = new Random(34);

    @Autowired private Status status;
    @Autowired private MessageRepository messagesRepository;
    @Autowired private OltRequestRepository oltRequestsRepository;

    private List<Integer> received_messages = new ArrayList<>();

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
    }

    private void update_yellow_and_red_target_messages(Orchestration orchestration) {
        if(orchestration.getSequence() == 2) {
            int additional_yellow_messages = (int) Math.floor(orchestration.getMessages() * 4 * 0.033);
            status.setTargetYellowMessages(status.getTargetYellowMessages() + additional_yellow_messages);
            log.info("Target yellow messages: " + status.getTargetYellowMessages());
            log.info("Generated yellow messages: " + status.getGeneratedYellowMessages());
            log.info("Target red messages: " + status.getTargetRedMessages());
            log.info("Generated red messages: " + status.getGeneratedRedMessages());
        }
        if(orchestration.getSequence() == 3) {
            int additional_yellow_messages = (int) Math.floor(orchestration.getMessages() * 4 * 0.033);
            int additional_red_messages = (int) Math.floor(orchestration.getMessages() * 4 * 0.033);
            status.setTargetYellowMessages(status.getTargetYellowMessages() + additional_yellow_messages);
            status.setTargetRedMessages(status.getTargetRedMessages() + additional_red_messages);
            log.info("Target yellow messages: " + status.getTargetYellowMessages());
            log.info("Generated yellow messages: " + status.getGeneratedYellowMessages());
            log.info("Target red messages: " + status.getTargetRedMessages());
            log.info("Generated red messages: " + status.getGeneratedRedMessages());
        } else {
            log.info("Target yellow messages: " + status.getTargetYellowMessages());
            log.info("Generated yellow messages: " + status.getGeneratedYellowMessages());
            log.info("Target red messages: " + status.getTargetRedMessages());
            log.info("Generated red messages: " + status.getGeneratedRedMessages());
        }
    }

    private List<OltRequest> generate_olt_requests(Orchestration orchestration, Message m) {
        List<OltRequest> generated_olt_requests = new ArrayList<>();
        if(orchestration.getSequence() == 1) {
            generated_olt_requests = SequenceGenerator.generate_requests_sequence(m, -1, 0);
        }
        if(orchestration.getSequence() == 2) {
            int left_yellow_messages = status.getTargetYellowMessages() - status.getGeneratedYellowMessages();
            int random_input = Math.min(left_yellow_messages, 4);
            int accessory_messages = random.nextInt(random_input);
            status.setGeneratedYellowMessages(status.getGeneratedYellowMessages() + accessory_messages);
            generated_olt_requests = SequenceGenerator.generate_requests_sequence(m, 0, accessory_messages);
        }
        if(orchestration.getSequence() == 3) {
            int acessory_type = random.nextInt(2);
            if(acessory_type == 0) {
               int left_yellow_messages = status.getTargetYellowMessages() - status.getGeneratedYellowMessages();
               int random_input = Math.min(left_yellow_messages, 4);
               int accessory_messages = random.nextInt(random_input);
               status.setGeneratedYellowMessages(status.getGeneratedYellowMessages() + accessory_messages);
               generated_olt_requests = SequenceGenerator.generate_requests_sequence(m, 0, accessory_messages);
            } else {
               int left_red_messages = status.getTargetRedMessages() - status.getGeneratedRedMessages();
               int random_input = Math.min(left_red_messages, 4);
               int accessory_messages = random.nextInt(random_input);
               status.setGeneratedRedMessages(status.getGeneratedRedMessages() + accessory_messages);
               generated_olt_requests = SequenceGenerator.generate_requests_sequence(m, 1, accessory_messages);
            }
        }
        return generated_olt_requests;
    }

    private void auto_consume_main_loop(Orchestration orchestration) {
        while(status.isOnGoingRun()) {
            Message m = Broker.fetch_message();
            if(m != null) {
                received_messages.add(m.getId());
                log.info("Received messages: " + converter.toJson(received_messages));
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
                List<OltRequest> generated_olt_requests = generate_olt_requests(orchestration, m);
                m = this.messagesRepository.save(m);
                List<OltRequest> sorted_olt_requests = new ArrayList<>();
                assert generated_olt_requests != null;
                for(OltRequest request : generated_olt_requests) {
                    request.setOriginMessage(m);
                    request = this.oltRequestsRepository.save(request);
                    sorted_olt_requests.add(request);
                }
                m.setStartedProcessing(new Date().getTime());
                m = this.messagesRepository.save(m);
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
                            m = this.messagesRepository.save(m);
                            timedout_requests++;
                        }
                        if(i == 3) {
                            Broker.inform_oracle_of_handling_end(m.getOlt());
                        }
                    }
                }
                if(timedout_requests == 0) {
                    m.setCompletedProcessing(new Date().getTime());
                    m.setSuccessful(true);
                    this.messagesRepository.save(m);
                }
            } else {
                // try { 
                //     Thread.sleep(500);
                // } catch(InterruptedException e) {
                //     e.printStackTrace();
                // }
                status.setComsumptionComplete(true);
            }
        }
    }

    public void handleOrchestration(String body) {
        Orchestration orchestration = converter.fromJson(body, Orchestration.class);
        log.info("Received orchestration: " + converter.toJson(orchestration));
        update_status_with_orchestration(orchestration);
        update_yellow_and_red_target_messages(orchestration); 
        boolean auto_consume = status.isOnGoingRun() && (status.getArchitecture() == 1 || status.getArchitecture() == 2);
        if(auto_consume) {
            auto_consume_main_loop(orchestration);
        } 
    }
}
