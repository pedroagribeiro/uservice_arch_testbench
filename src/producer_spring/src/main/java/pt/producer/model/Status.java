package pt.producer.model;

public class Status {

    private boolean on_going_run;
    private int current_message_id;

    public Status() {
        this.on_going_run = false;
        this.current_message_id = 0;
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

}
