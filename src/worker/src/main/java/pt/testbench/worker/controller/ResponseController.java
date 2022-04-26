package pt.testbench.worker.controller;

import com.google.gson.Gson;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.testbench.worker.model.Response;
import pt.testbench.worker.model.Status;

@RestController
@RequestMapping("/response")
public class ResponseController {

    private final String EXCHANGE_NAME = "";
    private RabbitTemplate rabbitTemplate;
    private final Gson converter = new Gson();

    @Autowired private Status status;

    public ResponseController(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("")
    public ResponseEntity<?> sendResponse(@RequestBody Response r) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "olt-" + status.getWorkerId() + "-response-queue", converter.toJson(r));
        return new ResponseEntity("Response submitted", HttpStatus.CREATED);
    }
}
