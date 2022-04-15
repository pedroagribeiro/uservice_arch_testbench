package pt.producer.controller;

import com.google.gson.Gson;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.producer.config.ConfigureOrchestrationQueue;
import pt.producer.model.Orchestration;
import pt.producer.model.OrchestrationNoId;
import pt.producer.model.Result;
import pt.producer.repository.ResultRepository;

@RestController
@RequestMapping("/orchestration")
public class OrchestrationController {

    private final RabbitTemplate rabbitTemplate;
    private static final Gson converter = new Gson();
    Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired private ResultRepository resultRepository;

    public OrchestrationController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("")
    public ResponseEntity<?> sendOrchestration(@RequestBody OrchestrationNoId orchestration) {
        Result r = new Result(orchestration, new Date().getTime());
        this.resultRepository.save(r);
        Result result_with_attributed_id = this.resultRepository.findResultWithHighestId().get(0);
        Orchestration o = new Orchestration(result_with_attributed_id.getId(), orchestration);
        rabbitTemplate.convertAndSend(ConfigureOrchestrationQueue.EXCHANGE_NAME, ConfigureOrchestrationQueue.QUEUE_NAME, converter.toJson(o));
        return new ResponseEntity<>("Orchestration submitted and has id: " + o.getId(), HttpStatus.CREATED);
    }

}
