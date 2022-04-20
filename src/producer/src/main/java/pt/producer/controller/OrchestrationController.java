package pt.producer.controller;

import com.google.gson.Gson;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequestMapping("/orchestration")
public class OrchestrationController {

    private final RabbitTemplate rabbitTemplate;
    private static final Gson converter = new Gson();

    @Autowired private ResultRepository resultRepository;

    public OrchestrationController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("")
    public ResponseEntity<?> sendOrchestration(@RequestBody OrchestrationNoId orchestration) {
        Result r = new Result(orchestration, new Date().getTime());
        r = this.resultRepository.save(r);
        Orchestration o = new Orchestration(r.getId(), orchestration);
        rabbitTemplate.convertAndSend(ConfigureOrchestrationQueue.EXCHANGE_NAME, ConfigureOrchestrationQueue.QUEUE_NAME, converter.toJson(o));
        return new ResponseEntity<>("Orchestration submitted and has id: " + o.getId(), HttpStatus.CREATED);
    }

}
