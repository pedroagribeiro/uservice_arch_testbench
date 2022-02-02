package pt.testbench.worker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueueController {

    @PostMapping("/enqueue")
    public ResponseEntity<?> enqueue(@RequestBody final String request) {
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
