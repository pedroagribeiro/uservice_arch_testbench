public class RequestReport {

    private long request_id;
    private String olt;
    private long total_time;
    private long time_broker_queue;
    private long time_worker_queue;

    private RequestReport(final long request_id, final String olt, final long total_time, final long time_broker_queue, final long time_worker_queue) {
        this.request_id = request_id;
        this.olt = olt;
        this.total_time = total_time;
        this.time_broker_queue = time_broker_queue;
        this.time_worker_queue = time_worker_queue;
    }

    @Override
    public String toString() {
        return "RequestReport{" +
                "request_id='" + request_id + '\'' +
                ", olt='" + olt + '\'' +
                ", total_time=" + total_time +
                ", time_broker_queue=" + time_broker_queue +
                ", time_worker_queue=" + time_worker_queue +
                '}';
    }
    
}
