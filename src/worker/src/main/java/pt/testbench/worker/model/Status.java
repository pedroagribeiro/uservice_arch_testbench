package pt.testbench.worker.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Status {

    private int worker_id;
    private boolean on_going_run;
    private int target_message_run;
    private int architecture;
    private String current_active_request;
    private Map<String, Boolean> request_satisfied;
    private int workers;

    public Status(final int worker_id) {
        this.worker_id = worker_id;
        this.on_going_run = false;
        this.target_message_run = 0;
        this.architecture = 1;
        this.current_active_request = "";
        this.request_satisfied = new ConcurrentHashMap<>();
        this.workers = 3;
    }

    public int getWorkerId() {
        return this.worker_id;
    }

    public boolean isOnGoingRun() {
        return this.on_going_run;
    }

    public int getTargetMessageRun() {
        return this.target_message_run;
    }

    public int getArchitecture() {
        return architecture;
    }

    public void setArchitecture(int architecture) {
        this.architecture = architecture;
    }

    public String getCurrentActiveRequest() {
        return current_active_request;
    }

    public void setIsOnGoingRun(final boolean on_going_run) {
        this.on_going_run = on_going_run;
    }

    public void setTargetMessageRun(final int target_message_run) {
        this.target_message_run = target_message_run;
    }

    public void setCurrentActiveRequest(String current_active_request) {
        this.current_active_request = current_active_request;
    }

    public Map<String, Boolean> getRequestSatisfied() {
        return request_satisfied;
    }

    public void setRequestSatisfied(Map<String, Boolean> request_satisfied) {
        this.request_satisfied = request_satisfied;
    }

    public int getWorkers() {
        return this.workers;
    }

    public void setWorkers(int workers) {
        this.workers = workers;
    }

}
