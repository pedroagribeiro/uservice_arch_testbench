package pt.testbench.broker_spring.producers;

import com.google.gson.Gson;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pt.testbench.broker_spring.configuration.ConfigureBrokerMessageQueue;
import pt.testbench.broker_spring.model.Message;

@RestController
public class MessageController {

    private final RabbitTemplate rabbitTemplate;
    private static final Gson converter = new Gson();

    public MessageController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/message")
    public ResponseEntity<?> sendMessage(@RequestBody Message message) {
        rabbitTemplate.convertAndSend(ConfigureBrokerMessageQueue.EXCHANGE_NAME, "", converter.toJson(message));
        return new ResponseEntity("Message submitted", HttpStatus.CREATED);
    }
}
