package pt.producer.controller;

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

    @PostMapping("/ended")
    public ResponseEntity<?> comunicateEndRun() {
        status.end_run();
        return new ResponseEntity<>("Updated the run status to: " + status.isOnGoingRun(), HttpStatus.OK);
    }
}
