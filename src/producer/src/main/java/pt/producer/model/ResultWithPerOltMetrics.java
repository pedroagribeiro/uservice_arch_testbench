package pt.producer.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResultWithPerOltMetrics {

    private int id;
    private int algorithm;
    private int sequence;
    private long theoretical_total_time_limit;
    private long verified_total_time;
    private int theoretical_timedout_requests_limit;
    private int verified_timedout_requests;
    private long start_instant;
    private long end_instant;
    private int olts;
    private int workers;
    private int requests;
    private String status;
    private List<PerOltProcessingTimeStripped> per_olt_metrics;

    public ResultWithPerOltMetrics(Result result, List<PerOltProcessingTime> perOltMetrics) {
        this.id = result.getId();
        this.algorithm = result.getAlgorithm();
        this.sequence = result.getSequence();
        this.theoretical_total_time_limit = result.getTheoreticalTotalTimeLimit();
        this.verified_total_time = result.getVerifiedTotalTime();
        this.theoretical_timedout_requests_limit = result.getTheoreticalTimedoutRequestsLimit();
        this.verified_timedout_requests = result.getVerifiedTimedoutRequests();
        this.start_instant = result.getStartInstant();
        this.end_instant = result.getEndInstant();
        this.olts = result.getOlts();
        this.workers = result.getWorkers();
        this.requests = result.getRequests();
        this.status = result.getStatus();
        this.per_olt_metrics = new ArrayList<>();
        for(PerOltProcessingTime r : perOltMetrics) {
           this.per_olt_metrics.add(new PerOltProcessingTimeStripped(r));
        }
    }

}
