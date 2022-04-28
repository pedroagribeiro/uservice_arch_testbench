package pt.producer.controller;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.producer.model.Status;

@RestController
@RequestMapping("/run")
public class RunController {

    @Autowired
    private Status status;

    Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @PostMapping("/ended")
    public ResponseEntity<?> comunicateEndRun() {
        status.end_run();
        log.info("Received ending run information");
        return new ResponseEntity<>("Updated the run status to: " + status.isOnGoingRun(), HttpStatus.OK);
    }
}
