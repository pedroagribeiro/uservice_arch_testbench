package pt.producer.model;

import java.util.ArrayList;
import java.util.List;

public class Status {

    private boolean on_going_run;
    private int current_message_id;
    private int current_run_workers;
    private List<Integer> finished_workers;

    public Status(int current_message_id) {
        this.on_going_run = false;
        this.current_message_id = current_message_id;
        this.finished_workers = new ArrayList<>();
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

}
