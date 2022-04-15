package pt.producer.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "results")
public class Result {

    public static final String[] availableStatus = { "on_going", "finished" };

    @Id
    @GeneratedValue(generator = "result_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "result_id_seq", sequenceName = "result_id_seq", allocationSize = 1)
    private int id;

    @Column(name = "algorithm", nullable = false)
    private int algorithm;

    @Column(name = "theoretical_total_time_limit")
    private long theoretical_total_time_limit;

    @Column(name = "verified_total_time")
    private long verified_total_time;

    @Column(name = "theoretical_timedout_requests_limit")
    private int theoretical_timedout_requests_limit;

    @Column(name = "verified_timedout_requests")
    private int verified_timedout_requests;

    @Column(name = "start_instant", nullable = false)
    private long start_instant;

    @Column(name = "end_instant")
    private long end_instant;

    @Column(name = "olts", nullable = false)
    private int olts;

    @Column(name = "workers", nullable = false)
    private int workers;

    @Column(name = "requests", nullable = false)
    private int requests;

    @Column(name = "status", nullable = false)
    private String status;

    public Result() {

    }

    public Result(OrchestrationNoId orchestration, long start_instant) {
        this.algorithm = orchestration.getAlgorithm();
        this.start_instant = start_instant;
        this.olts = orchestration.getOlts();
        this.workers = orchestration.getWorkers();
        this.requests = orchestration.getMessages();
        this.status = Result.availableStatus[0];
    }
    public Result(int algorithm, int olts, int workers, int requests, String status) {
        this.algorithm = algorithm;
        this.start_instant = new Date().getTime();
        this.olts = olts;
        this.workers = workers;
        this.requests = requests;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void set(int id) {
        this.id = id;
    }

    public int getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(int algorithm) {
        this.algorithm = algorithm;
    }

    public long getTheoreticalTotalTimeLimt() {
         return this.theoretical_total_time_limit;
    }

    public void setTheoreticalTotalTimeLimit(long theoretical_total_time_limit) {
        this.theoretical_total_time_limit = theoretical_total_time_limit;
    }

    public long getVerifiedTotalTime() {
        return this.verified_total_time;
    }

    public void setVerifiedTotalTime(long verified_total_time) {
        this.verified_total_time = verified_total_time;
    }

    public double getTheoreticalTimedoutRequestsLimit() {
        return this.theoretical_timedout_requests_limit;
    }

    public void setTheoreticalTimeoutRequestsLimit(int theoretical_timedout_requests_limit) {
        this.theoretical_timedout_requests_limit = theoretical_timedout_requests_limit;
    }

    public double getVerifiedTimedoutRequests() {
        return this.verified_timedout_requests;
    }

    public void setVerifiedTimedoutRequests(int verified_timedout_requests) {
        this.verified_timedout_requests = verified_timedout_requests;
    }

    public long getStartInstant() {
        return this.start_instant;
    }

    public void setStartInstant(long start_instant) {
        this.start_instant = start_instant;
    }

    public long getEndInstant() {
        return this.end_instant;
    }

    public void setEndInstant(long end_instant) {
        this.end_instant = end_instant;
    }
 
    public int getOlts() {
        return olts;
    }

    public void setOlts(int olts) {
        this.olts = olts;
    }

    public int getWorkers() {
        return workers;
    }

    public void setWorkers(int workers) {
        this.workers = workers;
    }

    public int getRequests() {
        return requests;
    }

    public void setRequests(int requests) {
        this.requests = requests;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", algorithm=" + algorithm +
                ", theoretical_total_time_limit=" + theoretical_total_time_limit +
                ", verified_total_time=" + verified_total_time +
                ", theoretical_timedout_requests_limit=" + theoretical_timedout_requests_limit +
                ", verified_timedout_requests=" + verified_timedout_requests +
                ", start_instant=" + start_instant +
                ", end_instant=" + end_instant +
                ", olts=" + olts +
                ", workers=" + workers +
                ", requests=" + requests +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return id == result.id && algorithm == result.algorithm && theoretical_total_time_limit == result.theoretical_total_time_limit && verified_total_time == result.verified_total_time && theoretical_timedout_requests_limit == result.theoretical_timedout_requests_limit && verified_timedout_requests == result.verified_timedout_requests && start_instant == result.start_instant && end_instant == result.end_instant && olts == result.olts && workers == result.workers && requests == result.requests && Objects.equals(status, result.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, algorithm, theoretical_total_time_limit, verified_total_time, theoretical_timedout_requests_limit, verified_timedout_requests, start_instant, end_instant, olts, workers, requests, status);
    }
}
