package pt.producer.controller;

import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.producer.model.*;
import pt.producer.repository.PerOltProcessingTimeRepository;
import pt.producer.repository.ResultRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/results")
@Api(value = "Results Controller")
@Tag(name = "Result service", description = "Service to access the result related endpoints")
public class ResultsController {

    @Autowired private ResultRepository resultsRepository;
    @Autowired private PerOltProcessingTimeRepository perOltProcessingTimesRepository;

    @ApiOperation(value = "Retrieve the results of all performed runs", response = List.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved results")
    })
    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<ResultWithPerOltMetrics>> retrieveAllResults() {
        Iterable<Result> all_results = resultsRepository.findAll();
        List<ResultWithPerOltMetrics> all_results_returnable = new ArrayList<>();
        for(Result r : all_results) {
            List<PerOltProcessingTime> olt_metrics = perOltProcessingTimesRepository.findByRunId(r.getId());
            ResultWithPerOltMetrics result_with_olt_metrics = new ResultWithPerOltMetrics(r, olt_metrics);
            all_results_returnable.add(result_with_olt_metrics);
        }
        return new ResponseEntity<>(all_results_returnable, HttpStatus.OK);
    }

    @ApiOperation(value = "Retrieve the result of a specified run", response = Result.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved run result"),
            @ApiResponse(code = 400, message = "There's result for the specified run identifier")
    })
    @ApiParam(name = "id", type = "Integer", required = true)
    @RequestMapping(value = "/single", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> retrieveRunResult(@RequestParam int id) {
        if(resultsRepository.existsById(id)) {
            Result r = resultsRepository.findById(id).get();
            List<PerOltProcessingTime> olt_metrics = perOltProcessingTimesRepository.findByRunId(r.getId());
            ResultWithPerOltMetrics result_with_olt_metrics = new ResultWithPerOltMetrics(r, olt_metrics);
            return new ResponseEntity<>(result_with_olt_metrics, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("There is no run with such identifier", HttpStatus.BAD_REQUEST);
        }
    }

    AlgorithmResult constructAlgorithmResultVerifiedTotalTime(int algorithm, List<Result> results) {
        List<Double> xx = new ArrayList<>();
        List<Double> yy = new ArrayList<>();
        List<Pair> paired_data = new ArrayList<>();
        for(Result result : results) {
            if(result.getAlgorithm() == algorithm) {
                paired_data.add(new Pair((double) result.getRequests(), (double) result.getVerifiedTotalTime()));
            }
        }
        paired_data.sort(Comparator.comparing(Pair::getX));
        for(Pair p : paired_data) {
            xx.add(p.getX());
            yy.add(p.getY());
        }
        return new AlgorithmResult(algorithm, xx, yy);
    }

    AlgorithmResult constructAlgorithmResultVerifiedTotalTimeouts(int algorithm, List<Result> results) {
        List<Double> xx = new ArrayList<>();
        List<Double> yy = new ArrayList<>();
        List<Pair> paired_data = new ArrayList<>();
        for(Result result : results) {
            if(result.getAlgorithm() == algorithm) {
                paired_data.add(new Pair((double) result.getRequests(), (double) result.getVerifiedTimedoutRequests()));
            }
        }
        paired_data.sort(Comparator.comparing(Pair::getX));
        for(Pair p : paired_data) {
            xx.add(p.getX());
            yy.add(p.getY());
        }
        return new AlgorithmResult(algorithm, xx, yy);
    }

    @ApiOperation(value = "Get graphics data concerning verified total time", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfuly got the graphics data"),
            @ApiResponse(code = 400, message = "Could not get graphics data")
    })
    @RequestMapping(value = "/verified_time_graphic/sequence/{sequence}/workers/{workers}/olts/{olts}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> get_verified_time_graphic_for_sequence(@PathVariable int sequence, @PathVariable int workers, @PathVariable int olts) {
        List<Result> results = this.resultsRepository.findRequestsWithGivenSequenceWorkersOlts(sequence, workers, olts);
        List<AlgorithmResult> returnable_results = new ArrayList<>();
        if(results.size() == 0) {
           return new ResponseEntity<>("There are no data for such parameters", HttpStatus.NOT_FOUND);
        } else {
            for(int i = 1; i <= 4; i++) {
               AlgorithmResult r = constructAlgorithmResultVerifiedTotalTime(i, results);
               returnable_results.add(r);
            }
            return new ResponseEntity<>(returnable_results, HttpStatus.OK);
        }
    }

    @ApiOperation(value = "Get graphics data concerning verified total timeouts", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfuly got the graphics data"),
            @ApiResponse(code = 400, message = "Could not get graphics data")
    })
    @RequestMapping(value = "/verified_timeouts_graphic/sequence/{sequence}/workers/{workers}/olts/{olts}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> get_verified_timeouts_graphic_for_sequence(@PathVariable int sequence, @PathVariable int workers, @PathVariable int olts) {
        List<Result> results = this.resultsRepository.findRequestsWithGivenSequenceWorkersOlts(sequence, workers, olts);
        List<AlgorithmResult> returnable_results = new ArrayList<>();
        if(results.size() == 0) {
            return new ResponseEntity<>("There are no data for such parameters", HttpStatus.NOT_FOUND);
        } else {
            for(int i = 1; i <= 4; i++) {
                AlgorithmResult r = constructAlgorithmResultVerifiedTotalTimeouts(i, results);
                returnable_results.add(r);
            }
            return new ResponseEntity<>(returnable_results, HttpStatus.OK);
        }
    }

    @RequestMapping("/combinations")
    public ResponseEntity<List<Pair>> workersOltsSimulatedCombinations() {
       Iterable<Result> results = this.resultsRepository.findAll();
       Map<Integer, List<Integer>> combinations_helper = new HashMap<>();
       List<Integer> keyList = new ArrayList<>();
       List<Pair> returnable_results = new ArrayList<>();
       for(Result r : results) {
           if(!combinations_helper.containsKey(r.getWorkers())) {
               combinations_helper.put(r.getWorkers(), new ArrayList<>());
           }
           if(!combinations_helper.get(r.getWorkers()).contains(r.getOlts())) {
               combinations_helper.get(r.getWorkers()).add(r.getOlts());
           }
       }
       for(Integer key : combinations_helper.keySet()) {
           Collections.sort(combinations_helper.get(key));
           keyList.add(key);
       }
       Collections.sort(keyList);
       for(Integer key : keyList) {
           List<Integer> olt_combs = combinations_helper.get(key);
           for(Integer olt_comb : olt_combs) {
               returnable_results.add(new Pair(key, olt_comb));
           }
       }
       return new ResponseEntity<>(returnable_results, HttpStatus.OK);
    }

    @RequestMapping("/sequences")
    public ResponseEntity<List<Integer>> simulatedSequences() {
        Iterable<Result> results = this.resultsRepository.findAll();
        List<Integer> used_sequences = new ArrayList<>();
        for(Result r : results) {
            if(!used_sequences.contains(r.getSequence())) {
                used_sequences.add(r.getSequence());
            }
        }
        return new ResponseEntity<>(used_sequences, HttpStatus.OK);
    }
}
