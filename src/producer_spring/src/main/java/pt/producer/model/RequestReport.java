package pt.producer.model;

public class RequestReport {

    private long request_id;
    private String olt;
    private long total_time;
    private long time_broker_queue;
    private long time_worker_queue;
    private long time_olt_queue;
    private boolean timedout;

    public RequestReport(final long request_id, final String olt, final long total_time, final long time_broker_queue, final long time_worker_queue, final long time_olt_queue, final boolean timedout) {
        this.request_id = request_id;
        this.olt = olt;
        this.total_time = total_time;
        this.time_broker_queue = time_broker_queue;
        this.time_worker_queue = time_worker_queue;
        this.time_olt_queue = time_olt_queue;
        this.timedout = timedout;
    }

    public long get_request_id() {
        return this.request_id;
    }

    public void set_request_id(final long request_id) {
        this.request_id = request_id;
    }

    public String get_olt() {
        return this.olt;
    }

    public void set_olt(final String olt) {
        this.olt = olt;
    }

    public long get_total_time() {
        return this.total_time;
    }

    public void set_total_time(final long total_time) {
        this.total_time = total_time;
    }

    public long get_time_broker_queue() {
        return this.time_broker_queue;
    }

    public void set_time_broker_queue(final long time_broker_queue) {
        this.time_broker_queue = time_broker_queue;
    }

    public long get_time_worker_queue() {
        return this.time_worker_queue;
    }

    public void set_time_worker_queue(final long time_worker_queue) {
        this.time_worker_queue = time_worker_queue;
    }

    public long get_time_olt_queue() {
        return this.time_olt_queue;
    }

    public void set_time_olt_queue(final long time_olt_queue) {
        this.time_olt_queue = time_olt_queue;
    }

    public boolean get_timedout() {
        return this.timedout;
    }

    public void set_timedout(final boolean timedout) {
        this.timedout = timedout;
    }

    @Override
    public String toString() {
        return "RequestReport{" +
                "request_id='" + request_id + '\'' +
                ", olt='" + olt + '\'' +
                ", total_time=" + total_time +
                ", time_broker_queue=" + time_broker_queue +
                ", time_worker_queue=" + time_worker_queue +
                ", time_olt_queue=" + time_olt_queue +
                ", timedout=" + timedout +
                '}';
    }
}
