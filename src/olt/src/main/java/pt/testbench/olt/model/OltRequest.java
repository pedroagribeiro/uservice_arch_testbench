package pt.testbench.olt.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.Objects;

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
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
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

    public void setLeftWorker(long leftWorker) {
        this.leftWorker = leftWorker;
    }

    public long getStartedBeingProcessedAtOlt() {
        return this.startedBeingProcessedAtOlt;
    }

    public void setStartedBeingProcessedAtOlt(long startedBeingProcessedAtOlt) {
        this.startedBeingProcessedAtOlt = startedBeingProcessedAtOlt;
    }

    public long getEndedBeingProcessedAtOlt() {
        return this.endedBeingProcessedAtOlt;
    }

    public void setEndedBeingProcessedAtOlt(long endedBeingProcessedAtOlt) {
        this.endedBeingProcessedAtOlt = endedBeingProcessedAtOlt;
    }

    public long getReturnedWorker() {
        return this.returnedWorker;
    }

    public void setReturnedWorker(long returnedWorker) {
        this.returnedWorker = returnedWorker;
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

    public void setResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return this.response;
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
                Objects.equals(id, that.id) &&
                issued_at == that.issued_at &&
                duration == that.duration &&
                timeout == that.timeout &&
                completed == that.completed &&
                Objects.equals(origin_message, that.origin_message) &&
                Objects.equals(response, that.response)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, issued_at, duration, timeout, completed, origin_message, response);
    }
    **/
}
