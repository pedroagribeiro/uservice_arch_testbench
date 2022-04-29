package pt.testbench.broker.controller;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pt.testbench.broker.config.ConfigureExchangeBean;
import pt.testbench.broker.config.ConfigureMessageQueue;
import pt.testbench.broker.model.Message;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/message")
public class MessageController {

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private final RabbitTemplate rabbitTemplate;
    private static final Gson converter = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();

    public MessageController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("")
    public ResponseEntity<?> sendMessage(@RequestBody Message message) {
        rabbitTemplate.convertAndSend(ConfigureExchangeBean.EXCHANGE_NAME, ConfigureMessageQueue.QUEUE_NAME, converter.toJson(message));
        log.info("Received " + converter.toJson(message));
        return new ResponseEntity("Message submitted", HttpStatus.CREATED);
    }

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Message consumeMessage() {
        org.springframework.amqp.core.Message m = rabbitTemplate.receive(ConfigureMessageQueue.QUEUE_NAME);
        if (m != null) {
            byte[] message_content = m.getBody();
            String message_json = new String(message_content, StandardCharsets.UTF_8);
            Message message = converter.fromJson(message_json, Message.class);
            log.info("Message sent to the worker: " + converter.toJson(message));
            return message;
        } else {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No message was found");
        }
    }

}
