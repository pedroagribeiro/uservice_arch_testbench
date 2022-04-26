package pt.testbench.worker.controller;

import com.google.gson.Gson;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.testbench.worker.config.ConfigureWorkerOrchestrationQueue;
import pt.testbench.worker.model.Orchestration;
import pt.testbench.worker.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/orchestration")
public class OrchestrationController {

    Logger log = LoggerFactory.getLogger(this.getClass().getName());

    private final String EXCHANGE_NAME = "";
    private RabbitTemplate rabbitTemplate;
    private static Gson converter = new Gson();

    @Autowired
    private Status status;

    public OrchestrationController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("")
    public ResponseEntity<?> sendOrchestration(@RequestBody Orchestration orchestration) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "worker-" + status.getWorkerId() + "-orchestration-queue", converter.toJson(orchestration));
        return new ResponseEntity<>("Worker " + status.getWorkerId() + " got the orchestration", HttpStatus.OK);
    }
}
