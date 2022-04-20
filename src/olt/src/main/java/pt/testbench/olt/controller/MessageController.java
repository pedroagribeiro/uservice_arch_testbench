package pt.testbench.olt.controller;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
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
import pt.testbench.olt.model.Status;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/message")
public class MessageController {

    private RabbitTemplate rabbitTemplate;
    private static Gson converter = new Gson();

    @Autowired
    private Status currentStatus;

    public MessageController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("")
    public ResponseEntity<?> sendMessage(@RequestBody OltRequest request) {
        currentStatus.getEnqueuedAtWorkerTimes().put(request.getId(), new Date().getTime());
        log.info("Got this request: " + converter.toJson(request));
        rabbitTemplate.convertAndSend(ConfigExchangeBean.EXCHANGE_NAME, ConfigOltMessageQueue.QUEUE_NAME, converter.toJson(request));
        return new ResponseEntity<>("Request submitted", HttpStatus.CREATED);
    }
}
