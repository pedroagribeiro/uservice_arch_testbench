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

import pt.testbench.worker.config.ConfigureWorkerMessageQueue;
import pt.testbench.worker.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import pt.testbench.worker.repository.MessageRepository;

import java.util.Date;


@RestController
@RequestMapping("/message")
public class MessageController {

    private final RabbitTemplate rabbitTemplate;
    private static final Gson converter = new Gson();
    @Autowired private MessageRepository messagesRepository;

    Logger log = LoggerFactory.getLogger(this.getClass().getName());

    public MessageController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate =  rabbitTemplate;
    }

    @PostMapping("")
    public ResponseEntity<?> sendMessage(@RequestBody Message m) {
        // this.messagesRepository.save(m);
        rabbitTemplate.convertAndSend(ConfigureWorkerMessageQueue.EXCHANGE_NAME, ConfigureWorkerMessageQueue.QUEUE_NAME, converter.toJson(m));
        return new ResponseEntity<>("Message submitted", HttpStatus.CREATED);
    }
}
