package pt.testbench.broker.handlers;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.testbench.broker.model.Orchestration;
import pt.testbench.broker.model.Status;

@Service
@Slf4j
public class ReceiveOrchestrationHandler {

    private final Gson converter = new Gson();

    @Autowired
    private SimpleMessageListenerContainer messageContainer;

    @Autowired
    private Status status;

    public void handleOrchestration(String body) {
       Orchestration orchestration = converter.fromJson(body, Orchestration.class);
       if(orchestration.getAlgorithm() == 1 || orchestration.getAlgorithm() == 2) {
           messageContainer.stop();
       } else {
           if(!messageContainer.isActive()) messageContainer.start();
       }
       log.info("Received orchestration: " + converter.toJson(orchestration));
       status.setArchitecture(orchestration.getAlgorithm());
       status.setWorkers(orchestration.getWorkers());
       status.setOlts(orchestration.getOlts());
       log.info("Running logic " + status.getArchitecture() + " ...");
    }
}
