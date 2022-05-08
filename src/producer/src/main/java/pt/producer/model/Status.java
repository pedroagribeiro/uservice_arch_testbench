package pt.producer.model;

import java.util.ArrayList;
import java.util.List;

public class Status {

    private boolean on_going_run;
    private int current_message_id;
    private int current_run_workers;
    private List<Integer> finished_workers;
    private List<Integer> sent_messages_workers;
    private List<Integer> sent_requests_workers;
    private List<Integer> sent_responses_workers;

    public Status(int current_message_id) {
        this.on_going_run = false;
        this.current_message_id = current_message_id;
        this.finished_workers = new ArrayList<>();
        this.sent_messages_workers = new ArrayList<>();
        this.sent_requests_workers = new ArrayList<>();
        this.sent_responses_workers = new ArrayList<>();
    }

    public boolean isOnGoingRun() {
        return this.on_going_run;
    }

    public int getCurrentMessageId() {
        return this.current_message_id;
    }

    public void setCurrentMessageId(final int current_message_id) {
        this.current_message_id = current_message_id;
    }

    public void start_run() {
        this.on_going_run = true;
    }

    public void end_run() {
        this.on_going_run = false;
    }

    public int getCurrentRunWorkers() {
        return this.current_run_workers;
    }

    public void setCurrentRunWorkers(int workers) {
        this.current_run_workers = workers;
    }

    public List<Integer> getFinishedWorkers() {
        return this.finished_workers;
    }

    public void setFinishedWorkers(List<Integer> finished_workers) {
        this.finished_workers = finished_workers;
    }

    public List<Integer> getSentMessagesWorkers() {
        return this.sent_messages_workers;
    }

    public void setSentMessagesWorkers(List<Integer> sent_messages_workers) {
        this.sent_messages_workers = sent_messages_workers;
    }

    public List<Integer> getSentRequestsWorkers() {
        return this.sent_requests_workers;
    }

    public void setSentRequestsWorkers(List<Integer> sent_requests_workers) {
        this.sent_requests_workers = sent_requests_workers;
    }

    public List<Integer> getSentResponsesWorkers() {
        return this.sent_responses_workers;
    }

    public void setSentResponsesWorkers(List<Integer> sent_responses_workers) {
        this.sent_responses_workers = sent_responses_workers;
    }

}
