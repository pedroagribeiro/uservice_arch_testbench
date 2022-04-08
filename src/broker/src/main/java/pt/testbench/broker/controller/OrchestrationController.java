package pt.testbench.broker.controller;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.testbench.broker.config.ConfigureExchangeBean;
import pt.testbench.broker.config.ConfigureOrchestrationQueue;
import pt.testbench.broker.model.Orchestration;

@RestController
@RequestMapping("/orchestration")
public class OrchestrationController {

    private final RabbitTemplate rabbitTemplate;
    private static final Gson converter = new Gson();

    private Logger log = LoggerFactory.getLogger(this.getClass().getName());

    public OrchestrationController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("")
    public ResponseEntity<?> sendOrchestration(@RequestBody Orchestration orchestration) {
       rabbitTemplate.convertAndSend(ConfigureExchangeBean.EXCHANGE_NAME, ConfigureOrchestrationQueue.QUEUE_NAME, converter.toJson(orchestration));
       return new ResponseEntity<>("Orchestration submitted", HttpStatus.CREATED);
    }
}
