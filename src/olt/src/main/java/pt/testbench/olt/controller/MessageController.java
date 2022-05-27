package pt.testbench.olt.controller;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.testbench.olt.config.ConfigExchangeBean;
import pt.testbench.olt.model.OltRequest;
import pt.testbench.olt.model.Status;
import pt.testbench.olt.model.StatusSingleton;

import java.util.Date;

@RestController
@RequestMapping("/message")
public class MessageController {

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private RabbitTemplate rabbitTemplate;
    private static Gson converter = new Gson();

    @Value("${olt.id}")
    private int olt_id;

    private Status currentStatus = StatusSingleton.getInstance();

    public MessageController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("")
    public ResponseEntity<?> sendMessage(@RequestBody OltRequest request) {
        currentStatus.getEnqueuedAtWorkerTimes().put(request.getId(), new Date().getTime());
        rabbitTemplate.convertAndSend(ConfigExchangeBean.EXCHANGE_NAME, "olt-" + olt_id + "-message-queue", converter.toJson(request));
        log.info("Enqueued request: " + request.getId());
        return new ResponseEntity<>("Request submitted", HttpStatus.CREATED);
    }
}
