package pt.testbench.worker.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Status {

    private int worker_id;
    private boolean on_going_run;
    private int target_message_run;
    private int architecture;
    private long current_active_request;
    private Map<Long, Boolean> request_satisfied;

    public Status(final int worker_id) {
        this.worker_id = worker_id;
        this.on_going_run = false;
        this.target_message_run = 0;
        this.architecture = 1;
        this.current_active_request = -1;
        this.request_satisfied = new ConcurrentHashMap<>();
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

    public long getCurrentActiveRequest() {
        return current_active_request;
    }

    public void setIsOnGoingRun(final boolean on_going_run) {
        this.on_going_run = on_going_run;
    }

    public void setTargetMessageRun(final int target_message_run) {
        this.target_message_run = target_message_run;
    }

    public void setCurrentActiveRequest(long current_active_request) {
        this.current_active_request = current_active_request;
    }

    public Map<Long, Boolean> getRequestSatisfied() {
        return request_satisfied;
    }

    public void setRequestSatisfied(Map<Long, Boolean> request_satisfied) {
        this.request_satisfied = request_satisfied;
    }

}
