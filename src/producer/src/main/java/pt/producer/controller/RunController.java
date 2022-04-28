package pt.producer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.producer.model.Status;

@Slf4j
@RestController
@RequestMapping("/run")
public class RunController {

    @Autowired
    private Status status;

    @PostMapping("/ended")
    public ResponseEntity<?> comunicateEndRun() {
        status.end_run();
        log.info("Received ending run information");
        return new ResponseEntity<>("Updated the run status to: " + status.isOnGoingRun(), HttpStatus.OK);
    }
}
