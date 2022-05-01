package pt.testbench.worker.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
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
import pt.testbench.worker.repository.MessageRepository;
import pt.testbench.worker.repository.OltRequestRepository;
import pt.testbench.worker.utils.SequenceGenerator;

@Service
@Slf4j
public class ReceiveMessageHandler {

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private final Gson converter = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    private Random random = new Random(34);

    @Autowired private MessageRepository messagesRepository;
    @Autowired private OltRequestRepository oltRequestsRepository;
    @Autowired private Status status;
    @Autowired AmqpAdmin amqpAdmin;

    List<Integer> received_messages = new ArrayList<>();

    private Callable<Boolean> request_satisfied(String request_id) {
        return () -> status.getRequestSatisfied().get(request_id);
    }

    private List<OltRequest> generate_olt_requests(Message m) {
        List<OltRequest> generated_olt_requests = new ArrayList<>();
        if(status.getSequence() == 1) {
            generated_olt_requests = SequenceGenerator.generate_requests_sequence(m, -1, 0);
        }
        if(status.getSequence() == 2) {
            int left_yellow_messages = status.getTargetYellowMessages() - status.getGeneratedYellowMessages();
            int random_input = Math.min(left_yellow_messages, 4);
            int accessory_messages = random.nextInt(random_input);
            status.setGeneratedYellowMessages(status.getGeneratedYellowMessages() + accessory_messages);
            generated_olt_requests = SequenceGenerator.generate_requests_sequence(m, 0, accessory_messages);
        }
        if(status.getSequence() == 3) {
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

    private int queue_size() {
        return (Integer) amqpAdmin.getQueueProperties("worker-" + status.getWorkerId() + "-message-queue").get("QUEUE_MESSAGE_COUNT");
    }

    public void handleMessage(String body) {
        Message m = converter.fromJson(body, Message.class);
        received_messages.add(m.getId());
        log.info("Received messages: " + converter.toJson(received_messages));
        log.info("Received a message: " + converter.toJson(m));
        List<OltRequest> generated_olt_requests = generate_olt_requests(m);
        m = this.messagesRepository.save(m);
        List<OltRequest> sorted_olt_requests = new ArrayList<>();
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
        if(queue_size() == 0) {
            status.setComsumptionComplete(true);
        }
        log.info("Queue size: " + queue_size());
    }
}
