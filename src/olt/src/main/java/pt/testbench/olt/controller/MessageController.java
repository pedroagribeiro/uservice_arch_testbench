package pt.testbench.olt.controller;

import com.google.gson.Gson;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.testbench.olt.config.ConfigExchangeBean;
import pt.testbench.olt.config.ConfigOltMessageQueue;
import pt.testbench.olt.model.Message;
import pt.testbench.olt.model.OltRequest;
import pt.testbench.olt.repository.OltRequestRepository;

import java.util.Date;

@RestController
@RequestMapping("/message")
public class MessageController {

    private RabbitTemplate rabbitTemplate;
    private static Gson converter = new Gson();

    @Autowired private OltRequestRepository oltRequestsRepository;

    public MessageController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("")
    public ResponseEntity<?> sendMessage(@RequestBody OltRequest m) {
        m.setEnqueuedAtOlt(new Date().getTime());
        OltRequest updated_request = this.oltRequestsRepository.save(m);
        rabbitTemplate.convertAndSend(ConfigExchangeBean.EXCHANGE_NAME, ConfigOltMessageQueue.QUEUE_NAME, converter.toJson(updated_request));
        return new ResponseEntity<>("Request submitted", HttpStatus.CREATED);
    }
}
