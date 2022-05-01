package pt.testbench.olt.controller;

import com.google.gson.Gson;
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
import pt.testbench.olt.config.ConfigExchangeBean;
import pt.testbench.olt.model.OltRequest;
import pt.testbench.olt.model.Status;
import java.util.Date;

@RestController
@RequestMapping("/message")
public class MessageController {

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
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
        rabbitTemplate.convertAndSend(ConfigExchangeBean.EXCHANGE_NAME, "olt-" + currentStatus.getOltId() + "-message-queue", converter.toJson(request));
        return new ResponseEntity<>("Request submitted", HttpStatus.CREATED);
    }
}
