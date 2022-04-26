package pt.testbench.worker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.testbench.worker.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/run")
public class RunController {

    Logger log = LoggerFactory.getLogger(this.getClass().getName());

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
        log.info("Updated run target to: " + target);
        return new ResponseEntity<>("Worker " + status.getWorkerId() +" has updated it's target to: " + target, HttpStatus.OK);
    }
}
