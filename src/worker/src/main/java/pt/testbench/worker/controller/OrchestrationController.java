package pt.testbench.worker.controller;

import com.google.gson.Gson;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.testbench.worker.config.ConfigureExchangeBean;
import pt.testbench.worker.config.ConfigureWorkerOrchestrationQueue;
import pt.testbench.worker.model.Orchestration;

@RestController
@RequestMapping("/orchestration")
public class OrchestrationController {

    private RabbitTemplate rabbitTemplate;
    private static Gson converter = new Gson();

    public OrchestrationController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("")
    public ResponseEntity<?> sendOrchestration(@RequestBody Orchestration orchestration) {
        rabbitTemplate.convertAndSend(ConfigureExchangeBean.EXCHANGE_NAME, ConfigureWorkerOrchestrationQueue.QUEUE_NAME, converter.toJson(orchestration));
        return new ResponseEntity<>("Orchestration submitted", HttpStatus.OK);
    }
}
