package pt.testbench.broker_spring.controller;

import com.google.gson.Gson;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.testbench.broker_spring.config.ConfigureExchangeBean;
import pt.testbench.broker_spring.config.ConfigureMessageQueue;
import pt.testbench.broker_spring.model.Message;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final RabbitTemplate rabbitTemplate;
    private static final Gson converter = new Gson();

    private Logger log = LoggerFactory.getLogger(this.getClass().getName());

    public MessageController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("")
    public ResponseEntity<?> sendMessage(@RequestBody Message message) {
        message.set_enqueued_at_broker(new Date().getTime());
        rabbitTemplate.convertAndSend(ConfigureExchangeBean.EXCHANGE_NAME, ConfigureMessageQueue.QUEUE_NAME, converter.toJson(message));
        return new ResponseEntity("Message submitted", HttpStatus.CREATED);
    }

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> consumeMessage() {
        org.springframework.amqp.core.Message m = rabbitTemplate.receive(ConfigureMessageQueue.QUEUE_NAME);
        if (m != null) {
            byte[] message_content = m.getBody();
            String message_json = new String(message_content, StandardCharsets.UTF_8);
            Message message = converter.fromJson(message_json, Message.class);
            message.set_dequeued_at_broker(new Date().getTime());
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No message was found", HttpStatus.NOT_FOUND);
        }
    }

}
