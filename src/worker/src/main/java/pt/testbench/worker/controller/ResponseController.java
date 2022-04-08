package pt.testbench.worker.controller;

import com.google.gson.Gson;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.testbench.worker.config.ConfigureExchangeBean;
import pt.testbench.worker.config.ConfigureWorkerResponseQueue;
import pt.testbench.worker.model.Response;

@RestController
@RequestMapping("/response")
public class ResponseController {

    private RabbitTemplate rabbitTemplate;
    private final Gson converter = new Gson();

    public ResponseController(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("")
    public ResponseEntity<?> sendResponse(@RequestBody Response r) {
        rabbitTemplate.convertAndSend(ConfigureExchangeBean.EXCHANGE_NAME, ConfigureWorkerResponseQueue.QUEUE_NAME, converter.toJson(r));
        return new ResponseEntity("Response submitted", HttpStatus.CREATED);
    }
}
