package pt.testbench.worker.controller;

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

import pt.testbench.worker.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import pt.testbench.worker.model.Status;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final String EXCHANGE_NAME = "";
    private final RabbitTemplate rabbitTemplate;
    private static final Gson converter = new Gson();

    @Autowired private Status status;

    Logger log = LoggerFactory.getLogger(this.getClass().getName());

    public MessageController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate =  rabbitTemplate;
    }

    @PostMapping("")
    public ResponseEntity<?> sendMessage(@RequestBody Message m) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "worker-" + status.getWorkerId() + "-message-queue", converter.toJson(m));
        return new ResponseEntity<>("Message submitted", HttpStatus.CREATED);
    }
}
