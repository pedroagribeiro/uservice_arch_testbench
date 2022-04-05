package pt.testbench.worker_spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.testbench.worker_spring.model.Status;

@RestController
@RequestMapping("/run")
public class RunController {

    @Autowired
    private Status status;

    @PostMapping("/started")
    public ResponseEntity<?> startedRun() {
        this.status.setIsOnGoingRun(true);
        return new ResponseEntity<>("Ongoing run: " + status.isOnGoingRun(), HttpStatus.OK);
    }

    @PostMapping("/ended")
    public ResponseEntity<?> endedRun() {
        this.status.setIsOnGoingRun(false);
        return new ResponseEntity<>("Ongoing run: " + status.isOnGoingRun(), HttpStatus.OK);
    }

    @PostMapping("/target")
    public ResponseEntity<?> updateRunTarget(@RequestParam int target) {
        this.status.setTargetMessageRun(target);
        return new ResponseEntity<>("Target has been updated to: " + target, HttpStatus.OK);
    }
}
