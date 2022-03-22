package pt.testbench.broker_spring.producers;

import com.google.gson.Gson;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pt.testbench.broker_spring.configuration.ConfigureBrokerMessageQueue;
import pt.testbench.broker_spring.configuration.ConfigureBrokerOrchestrationQueue;
import pt.testbench.broker_spring.model.Orchestration;

@RestController
public class OrchestrationController {

    private final RabbitTemplate rabbitTemplate;
    private static final Gson converter = new Gson();

    public OrchestrationController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/orchestration")
    public ResponseEntity<?> sendOrchestration(@RequestBody Orchestration orchestration) {
       rabbitTemplate.convertAndSend(ConfigureBrokerOrchestrationQueue.EXCHANGE_NAME, "", converter.toJson(orchestration));
       return new ResponseEntity<>("Orchestration submitted", HttpStatus.CREATED);
    }
}
