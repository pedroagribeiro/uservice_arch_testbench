package pt.testbench.worker.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Status {

    private int worker_id;
    private boolean on_going_run;
    private int target_message_run;
    private boolean target_reached;
    private int architecture;
    private int sequence;
    private String current_active_request;
    private Map<String, Boolean> request_satisfied;
    private int workers;

    private Map<Integer, Message> current_run_messages;
    private Map<String, OltRequest> current_run_requests;
    private Map<String, Response> current_run_responses;

    private boolean consumption_complete;

    private AtomicInteger timedout_provisions = new AtomicInteger();

    public Status(final int worker_id) {
        this.worker_id = worker_id;
        this.on_going_run = false;
        this.target_message_run = 0;
        this.target_reached = false;
        this.architecture = 1;
        this.sequence = 1;
        this.current_active_request = "";
        this.request_satisfied = new ConcurrentHashMap<>();
        this.workers = 3;
        this.consumption_complete = false;
        this.timedout_provisions.set(0);
        this.current_run_messages = new HashMap<>();
        this.current_run_requests = new HashMap<>(); 
        this.current_run_responses = new HashMap<>();
    };

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

    public int getSequence() { return sequence; }

    public void setSequence(int sequence) { this.sequence = sequence; }

    public String getCurrentActiveRequest() {
        return current_active_request;
    }

    public void setIsOnGoingRun(final boolean on_going_run) {
        this.on_going_run = on_going_run;
    }

    public void setTargetMessageRun(final int target_message_run) {
        this.target_message_run = target_message_run;
    }

    public void setTargetReached(boolean target_reached) {
        this.target_reached = target_reached;
    }

    public boolean getTargetReached() {
        return this.target_reached;
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

    public boolean getConsumptionComplete() {
        return this.consumption_complete;
    }

    public void setComsumptionComplete(boolean consumption_complete) {
        this.consumption_complete = consumption_complete;
    }

    public AtomicInteger getTimedoutProvisions() {
        return this.timedout_provisions;
    }

    public void setCurrentRunMessages(Map<Integer, Message> current_run_messages) {
        this.current_run_messages = current_run_messages;
    }

    public Map<Integer, Message> getCurrentRunMessages() {
        return this.current_run_messages;
    }

    public void setCurrentRunRequests(Map<String, OltRequest> current_run_requests) {
        this.current_run_requests = current_run_requests;
    }

    public Map<String, OltRequest> getCurrentRunRequests() {
        return this.current_run_requests;
    }

    public void setCurrentRunResponses(Map<String, Response> current_run_responses) {
        this.current_run_responses = current_run_responses;
    }

    public Map<String, Response> getCurrentRunResponses() {
        return this.current_run_responses;
    }
}
