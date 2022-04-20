package pt.producer.model;

import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "olt_requests")
@Proxy(lazy = false)
public class OltRequest {

    @Id
    private String id;

    @Column(name = "issued_at", nullable = false)
    private long issuedAt;

    @Column(name = "duration", nullable = false)
    private long duration;

    @Column(name = "timeout", nullable = false)
    private long timeout;

    @Column(name = "left_worker")
    private long leftWorker;

    @Column(name = "started_being_processed_at_olt")
    private long startedBeingProcessedAtOlt;

    @Column(name = "ended_being_processed_at_olt")
    private long endedBeingProcessedAtOlt;

    @Column(name = "returned_worker")
    private long returnedWorker;

    @Column(name = "completed")
    private long completed;

    @Column(name = "not_processed")
    private boolean notProcessed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_message_id", referencedColumnName = "id")
    private Message originMessage;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id", referencedColumnName = "id")
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

    public void setNot_processed(boolean not_processed) {
        this.notProcessed = not_processed;
    }

    public Response getResponse() {
        return this.response;
    }

    public Message getOriginMessage() {
        return this.originMessage;
    }

    @Override
    public String toString() {
        return "OltRequest{" +
                "id='" + id + '\'' +
                ", issued_at=" + issuedAt +
                ", duration=" + duration +
                ", timeout=" + timeout +
                ", leftWorker= " + leftWorker +
                ", startedBeingProcessedAtOlt= " + startedBeingProcessedAtOlt +
                ", endedBeingProcessedAtOlt= " + endedBeingProcessedAtOlt +
                ", returnedWorker= " + returnedWorker +
                ", completed=" + completed +
                ", origin_message=" + originMessage +
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
                issuedAt == that.issuedAt &&
                duration == that.duration &&
                timeout == that.timeout &&
                leftWorker == that.leftWorker &&
                startedBeingProcessedAtOlt == that.startedBeingProcessedAtOlt &&
                endedBeingProcessedAtOlt == that.endedBeingProcessedAtOlt &&
                returnedWorker == that.returnedWorker &&
                completed == that.completed &&
                Objects.equals(originMessage, that.originMessage) &&
                Objects.equals(response, that.response)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, issuedAt, duration, timeout, leftWorker, startedBeingProcessedAtOlt, endedBeingProcessedAtOlt, returnedWorker, completed, originMessage, response);
    }
}
