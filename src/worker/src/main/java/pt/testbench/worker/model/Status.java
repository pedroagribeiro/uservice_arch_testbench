package pt.testbench.worker.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    private int generated_yellow_messages = 0;
    private int target_yellow_messages = 0;
    private int generated_red_messages = 0;
    private int target_red_messages = 0;

    private boolean consumption_complete;

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

    public int getGeneratedYellowMessages() {
        return this.generated_yellow_messages;
    }

    public void setGeneratedYellowMessages(int generated_yellow_messages) {
        this.generated_yellow_messages = generated_yellow_messages;
    }

    public int getTargetYellowMessages() {
        return this.target_yellow_messages;
    }

    public void setTargetYellowMessages(int target_yellow_messages) {
        this.target_yellow_messages = target_yellow_messages;
    }

    public int getGeneratedRedMessages() {
        return this.generated_red_messages;
    }

    public void setGeneratedRedMessages(int generated_red_messages) {
        this.generated_red_messages = generated_red_messages;
    }

    public int getTargetRedMessages() {
        return this.target_red_messages;
    }

    public void setTargetRedMessages(int target_red_messages) {
        this.target_red_messages = target_red_messages;
    }

    public boolean getConsumptionComplete() {
        return this.consumption_complete;
    }

    public void setComsumptionComplete(boolean consumption_complete) {
        this.consumption_complete = consumption_complete;
    }
}
