package pt.producer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.producer.model.*;
import pt.producer.repository.MessageRepository;
import pt.producer.repository.PerOltProcessingTimeRepository;
import pt.producer.repository.ResultRepository;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/results")
public class ResultsController {

    @Autowired private MessageRepository messagesRepository;
    @Autowired private ResultRepository resultsRepository;
    @Autowired private PerOltProcessingTimeRepository perOltProcessingTimesRepository;

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

    List<Pair> construct_olt_worker_distribution(List<Message> messages, String olt) {
        List<Pair> r = new ArrayList<>();
        List<Message> filtered_messages = messages.stream().filter(m -> Objects.equals(m.getOlt(), olt)).collect(Collectors.toList());
        Map<Integer, Integer> count_map = new HashMap<>();
        for(Message m : filtered_messages) {
            if(!count_map.containsKey(m.getWorker())) {
                count_map.put(m.getWorker(), 1);
            } else {
                count_map.put(m.getWorker(), count_map.get(m.getWorker()) + 1);
            }
        }
        List<Integer> workers = new ArrayList<>(count_map.keySet());
        Collections.sort(workers);
        for(Integer i : workers) {
            r.add(new Pair(i, count_map.get(i)));
        }
        return r;
    }

    @RequestMapping("/handler_distribution_by_olt/{run_id}")
    public ResponseEntity<?> handlerDistributionByOltByRun(@PathVariable int run_id) {
        if(!this.resultsRepository.existsById(run_id)) {
            return new ResponseEntity<>("There's no saved run with such id", HttpStatus.NOT_FOUND);
        } else {
            Map<String, List<Pair>> r = new HashMap<>();
            int starting_id = 0;
            for(int i = 1; i < run_id; i++) {
                Result run = this.resultsRepository.findById(i).get();
                starting_id += run.getRequests();
            }
            Result current_run = this.resultsRepository.findById(run_id).get();
            List<Message> required_messages = this.messagesRepository.findMessagesBetweenIdRange(starting_id + 1, starting_id + current_run.getRequests() - 1);
            for (int i = 0; i < current_run.getOlts(); i++) {
                r.put(String.valueOf(i), construct_olt_worker_distribution(required_messages, String.valueOf(i)));
            }
            return new ResponseEntity<>(r, HttpStatus.OK);
        }
    }

}
