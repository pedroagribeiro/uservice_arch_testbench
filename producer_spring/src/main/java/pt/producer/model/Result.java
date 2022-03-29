package pt.producer.model;

import javax.persistence.*;

@Entity
@Table(name = "results")
public class Result {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int run;

    @Column(name = "algorithm", nullable = false)
    private int algorithm;

    @Column(name = "avg_time_total")
    private double avg_time_total;

    @Column(name = "avg_time_broker_queue")
    private double avg_time_broker_queue;

    @Column(name = "avg_time_worker_queue")
    private double avg_time_worker_queue;

    @Column(name = "avg_time_olt_queue")
    private double avg_time_olt_queue;

    @Column(name = "olts")
    private int olts;

    @Column(name = "workers", nullable = false)
    private int workers;

    @Column(name = "requests", nullable = false)
    private int requests;

    @Column(name = "timedout")
    private double timedout;

    @Column(name = "status", nullable = false)
    private String status;

    public Result() {

    }

    public Result(OrchestrationNoId orchestration) {
        this.algorithm = orchestration.get_algorithm();
        this.olts = orchestration.get_olts();
        this.workers = orchestration.get_workers();
        this.requests = orchestration.get_messages();
        this.status = "ON GOING";
    }
    public Result(int algorithm, int olts, int workers, int requests, String status) {
        this.algorithm = algorithm;
        this.olts = olts;
        this.workers = workers;
        this.requests = requests;
        this.status = status;
    }

    public int getRun() {
        return run;
    }

    public void setRun(int run) {
        this.run = run;
    }

    public int getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(int algorithm) {
        this.algorithm = algorithm;
    }

    public double getAvg_time_total() {
        return avg_time_total;
    }

    public void setAvg_time_total(double avg_time_total) {
        this.avg_time_total = avg_time_total;
    }

    public double getAvg_time_broker_queue() {
        return avg_time_broker_queue;
    }

    public void setAvg_time_broker_queue(double avg_time_broker_queue) {
        this.avg_time_broker_queue = avg_time_broker_queue;
    }

    public double getAvg_time_worker_queue() {
        return avg_time_worker_queue;
    }

    public void setAvg_time_worker_queue(double avg_time_worker_queue) {
        this.avg_time_worker_queue = avg_time_worker_queue;
    }

    public double getAvg_time_olt_queue() {
        return avg_time_olt_queue;
    }

    public void setAvg_time_olt_queue(double avg_time_olt_queue) {
        this.avg_time_olt_queue = avg_time_olt_queue;
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

    public double getTimedout() {
        return timedout;
    }

    public void setTimedout(double timedout) {
        this.timedout = timedout;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
