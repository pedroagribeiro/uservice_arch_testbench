package pt.testbench.broker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.testbench.broker.model.Status;


@RestController
@RequestMapping("/management")
public class ManagementController {

    @Autowired
    private Status status;

    @PostMapping("")
    public ResponseEntity<?> startHandlingRequest(@RequestParam String olt, @RequestParam int worker) {
        status.getOracle().put(olt, worker);
        return new ResponseEntity<>("The oracle has been updated with that information", HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity<?> endHandlingRequest(@RequestParam String olt) {
        status.getOracle().remove(olt);
        return new ResponseEntity<>("The oracle has been updated with that information", HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> anyoneHandlingRequestForGivenOlt(@RequestParam String olt) {
        boolean exists = status.getOracle().containsKey(olt);
        if(exists) {
            return new ResponseEntity<>(status.getOracle().get(olt), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("None of the workers are handling requests for such olt", HttpStatus.NOT_FOUND);
        }
    }
}
