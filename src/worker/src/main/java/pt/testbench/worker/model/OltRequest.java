package pt.testbench.worker.model;

import lombok.Data;
import java.util.Date;

@Data
public class OltRequest {

    private String id;
    private long issuedAt;
    private long duration;
    private long timeout;
    private long leftWorker;
    private long startedBeingProcessedAtOlt;
    private long endedBeingProcessedAtOlt;
    private long returnedWorker;
    private long completed;
    private boolean notProcessed;
    private Message originMessage;
    private Response response;

    public OltRequest() {

    }

    public OltRequest(final String id, final long duration, final long timeout) {
        this.id = id;
        this.issuedAt = new Date().getTime();
        this.duration = duration;
        this.timeout = timeout;
        this.notProcessed = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(long issued_at) {
        this.issuedAt = issued_at;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getLeftWorker() {
        return this.leftWorker;
    }

    public void setLeftWorker(long left_worker) {
        this.leftWorker = left_worker;
    }

    public long getStartedBeingProcessedAtOlt() {
        return this.startedBeingProcessedAtOlt;
    }

    public void setStartedBeingProcessedAtOlt(long started_being_processed_at_olt) {
        this.startedBeingProcessedAtOlt = started_being_processed_at_olt;
    }

    public long getEndedBeingProcessedAtOlt() {
        return this.endedBeingProcessedAtOlt;
    }

    public void setEndedBeingProcessedAtOlt(long ended_being_processed_at_olt) {
        this.endedBeingProcessedAtOlt = ended_being_processed_at_olt;
    }

    public long getReturnedWorker() {
        return this.returnedWorker;
    }

    public void setReturnedWorker(long returned_worker) {
        this.returnedWorker = returned_worker;
    }

    public long getCompleted() {
        return completed;
    }

    public void setCompleted(long completed) {
        this.completed = completed;
    }

    public boolean getNotProcessed() {
        return this.notProcessed;
    }

    public void setNotProcessed(boolean not_processed) {
        this.notProcessed = not_processed;
    }

    public Response getResponse() {
        return this.response;
    }

    public void setOriginMessage(Message origin_message) {
        this.originMessage = origin_message;
    }

    public Message getOriginMessage() {
        return this.originMessage;
    }

    /**
    @Override
    public String toString() {
        return "OltRequest{" +
                "id='" + id + '\'' +
                ", issued_at=" + issued_at +
                ", duration=" + duration +
                ", timeout=" + timeout +
                ", enqueued_at_olt=" + enqueued_at_olt +
                ", dequeued_at_olt=" + dequeued_at_olt +
                ", completed=" + completed +
                ", origin_message=" + origin_message +
                ", response=" + response +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OltRequest that = (OltRequest) o;
        return (
                id == that.id &&
                        issued_at == that.issued_at &&
                        duration == that.duration &&
                        timeout == that.timeout &&
                        enqueued_at_olt == that.enqueued_at_olt &&
                        dequeued_at_olt == that.dequeued_at_olt &&
                        completed == that.completed &&
                        Objects.equals(origin_message, that.origin_message) &&
                        Objects.equals(response, that.response)
        );
    }

    @Override
        return Objects.hash(id, issued_at, duration, timeout, enqueued_at_olt, dequeued_at_olt, completed, origin_message, response);
    }
    **/
}
