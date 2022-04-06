package pt.testbench.broker_spring.handlers;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.testbench.broker_spring.model.Orchestration;
import pt.testbench.broker_spring.model.Status;

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
       if(orchestration.get_algorithm() == 1 || orchestration.get_algorithm() == 2) {
           messageContainer.stop();
       } else {
           if(!messageContainer.isActive()) messageContainer.start();
       }
       log.info("Received orchestration: " + converter.toJson(orchestration));
       status.setArchitecture(orchestration.get_algorithm());
       status.setWorkers(orchestration.get_workers());
       status.setOlts(orchestration.get_olts());
       log.info("Running logic " + status.getArchitecture() + " ...");
    }
}
