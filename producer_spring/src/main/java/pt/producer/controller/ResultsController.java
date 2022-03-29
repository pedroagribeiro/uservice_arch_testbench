package pt.producer.controller;

import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.producer.model.Result;
import pt.producer.repository.ResultRepository;

import java.util.List;

@RestController
@RequestMapping("/results")
@Api(value = "Results Controller")
@Tag(name = "Result service", description = "Service to access the result related endpoints")
public class ResultsController {

    @Autowired private ResultRepository resultsRepository;

    @ApiOperation(value = "Retrieve the results of all performed runs", response = List.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved results")
    })
    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Result>> retrieveAllResults() {
        List<Result> all_results = resultsRepository.findAll();
        return new ResponseEntity<>(all_results, HttpStatus.OK);
    }

    @ApiOperation(value = "Retrieve the result of a specified run", response = Result.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved run result"),
            @ApiResponse(code = 400, message = "There's result for the specified run identifier")
    })
    @ApiParam(name = "id", type = "Integer", required = true)
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> retrieveRunResult(@RequestParam int id) {
        if(resultsRepository.existsById(id)) {
            Result r = resultsRepository.findById(id).get();
            return new ResponseEntity<>(r, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("There is no run with such identifier", HttpStatus.BAD_REQUEST);
        }
    }
}
