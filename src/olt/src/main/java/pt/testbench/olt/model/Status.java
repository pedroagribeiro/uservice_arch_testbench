package pt.testbench.olt.model;

import java.util.HashMap;
import java.util.Map;

public class Status {

    private int olt_id;
    private Map<String, Long> enqueued_at_worker_times;

    public Status(int olt_id) {
        this.olt_id = olt_id;
        this.enqueued_at_worker_times = new HashMap<>();
    }

    public void setOltId(int olt_id) {
        this.olt_id = olt_id;
    }

    public int getOltId() {
        return this.olt_id;
    }

    public void setEnqueuedAtWorkerTimes(final Map<String, Long> enqueued_at_worker_times) {
        this.enqueued_at_worker_times = enqueued_at_worker_times;
    }

    public Map<String, Long> getEnqueuedAtWorkerTimes() {
        return this.enqueued_at_worker_times;
    }
}
