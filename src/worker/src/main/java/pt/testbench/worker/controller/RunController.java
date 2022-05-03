package pt.testbench.worker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.testbench.worker.communication.Producer;
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

    @PostMapping("/target")
    public ResponseEntity<?> updateRunTarget(@RequestParam int target) {
        this.status.setTargetMessageRun(target);
        log.info("Updated run target to: " + target);
        return new ResponseEntity<>("Worker " + status.getWorkerId() +" has updated it's target to: " + target, HttpStatus.OK);
    }

    @PostMapping("/target_reached")
    public ResponseEntity<?> targetHasBeenReached() {
        this.status.setTargetReached(true);
        log.info("Got information that the target has been reached");
        if(status.getRequestSatisfied().size() == 0 && status.getConsumptionComplete()) {
            Producer.inform_run_is_over(status.getWorkerId());
        }
        return new ResponseEntity<>("Target has been reached!", HttpStatus.OK);
    }
}
