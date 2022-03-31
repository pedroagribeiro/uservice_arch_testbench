package pt.producer.controller;

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
    @Qualifier("currentStatus")
    private Status status;

    Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @PostMapping("/ended")
    public ResponseEntity<?> comunicateEndRun() {
        log.info("on_going: " + status.isOnGoingRun() + ", current_mess_id: " + status.getCurrentMessageId());
        status.end_run();
        log.info("on_going: " + status.isOnGoingRun() + ", current_mess_id: " + status.getCurrentMessageId());
        log.info("Received ending run information");
        return new ResponseEntity<>("Updated the run status to: " + status.isOnGoingRun(), HttpStatus.OK);
    }
}
