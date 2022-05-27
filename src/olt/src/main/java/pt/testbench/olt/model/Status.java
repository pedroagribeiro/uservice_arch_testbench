package pt.testbench.olt.model;

import java.util.HashMap;
import java.util.Map;

public class Status {

    private Map<String, Long> enqueued_at_worker_times;

    public Status() {
        this.enqueued_at_worker_times = new HashMap<>();
    }

    public void setEnqueuedAtWorkerTimes(final Map<String, Long> enqueued_at_worker_times) {
        this.enqueued_at_worker_times = enqueued_at_worker_times;
    }

    public Map<String, Long> getEnqueuedAtWorkerTimes() {
        return this.enqueued_at_worker_times;
    }
}
