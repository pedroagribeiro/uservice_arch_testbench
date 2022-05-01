package pt.producer.controller;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.producer.model.Status;

@RestController
@RequestMapping("/run")
public class RunController {

    @Autowired
    private Status status;

    Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @PostMapping("/ended")
    public ResponseEntity<?> comunicateEndRun(@RequestParam int worker) {
        status.getFinishedWorkers().add(worker);
        if(status.getFinishedWorkers().size() == status.getCurrentRunWorkers()) {
            status.end_run();
            log.info("Run is completely finished");
            return new ResponseEntity<>("All workers have finished. Updated the run status to: " + status.isOnGoingRun(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Worker " + worker + "has finished the run.", HttpStatus.OK);
    }
}
