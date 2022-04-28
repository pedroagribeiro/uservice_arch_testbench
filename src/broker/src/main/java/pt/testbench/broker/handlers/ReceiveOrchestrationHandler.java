package pt.testbench.broker.handlers;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.testbench.broker.model.Orchestration;
import pt.testbench.broker.model.Status;

@Service
public class ReceiveOrchestrationHandler {

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private final Gson converter = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();

    @Autowired
    private SimpleMessageListenerContainer messageContainer;

    @Autowired
    private Status status;

    private void manage_message_container_action(Orchestration orchestration) {
         if(orchestration.getAlgorithm() == 1 || orchestration.getAlgorithm() == 2) {
           messageContainer.stop();
       } else {
           if(!messageContainer.isActive()) messageContainer.start();
       }
    }

    public void handleOrchestration(String body) {
       Orchestration orchestration = converter.fromJson(body, Orchestration.class);
       manage_message_container_action(orchestration);
       log.info("Received orchestration: " + converter.toJson(orchestration));
       status.setArchitecture(orchestration.getAlgorithm());
       status.setWorkers(orchestration.getWorkers());
       status.setOlts(orchestration.getOlts());
       log.info("Running logic " + status.getArchitecture() + " ...");
    }
}
