package pt.testbench.olt_spring.controller;

import com.google.gson.Gson;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.testbench.olt_spring.config.ConfigExchangeBean;
import pt.testbench.olt_spring.config.ConfigOltMessageQueue;
import pt.testbench.olt_spring.model.Message;

import java.util.Date;

@RestController
@RequestMapping("/message")
public class MessageController {

    private RabbitTemplate rabbitTemplate;
    private static Gson converter = new Gson();

    public MessageController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("")
    public ResponseEntity<?> sendMessage(@RequestBody Message m) {
        m.set_enqueued_at_olt(new Date().getTime());
        rabbitTemplate.convertAndSend(ConfigExchangeBean.EXCHANGE_NAME, ConfigOltMessageQueue.QUEUE_NAME, converter.toJson(m));
        return new ResponseEntity<>("Message submitted", HttpStatus.CREATED);
    }
}
