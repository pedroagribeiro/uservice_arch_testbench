package pt.producer.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "olt_requests")
public class OltRequest {

    @Id
    private String id;

    @Column(name = "issued_at", nullable = false)
    private long issued_at;

    @Column(name = "duration", nullable = false)
    private long duration;

    @Column(name = "timeout", nullable = false)
    private long timeout;

    @Column(name = "enqueued_at_olt")
    private long enqueued_at_olt;

    @Column(name = "dequeued_at_olt")
    private long dequeued_at_olt;

    @Column(name = "completed")
    private long completed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_message_id", referencedColumnName = "id")
    private Message origin_message;

    @OneToOne(mappedBy = "origin_request")
    private Response response;


    public OltRequest() {

    }

    public OltRequest(final String id, final long duration, final long timeout) {
       this.id = id;
       this.issued_at = new Date().getTime();
       this.duration = duration;
       this.timeout = timeout;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getIssuedAt() {
        return issued_at;
    }

    public void setIssuedAt(long issued_at) {
        this.issued_at = issued_at;
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

    public long getEnqueuedAtOlt() {
        return enqueued_at_olt;
    }

    public void setEnqueuedAtOlt(long enqueued_at_olt) {
        this.enqueued_at_olt = enqueued_at_olt;
    }

    public long getDequeuedAtOlt() {
        return dequeued_at_olt;
    }

    public void setDequeuedAtOlt(long dequeued_at_olt) {
        this.dequeued_at_olt = dequeued_at_olt;
    }

    public long getCompleted() {
        return completed;
    }

    public void setCompleted(long completed) {
        this.completed = completed;
    }

    public Response getResponse() {
        return this.response;
    }

    public Message getOriginMessage() {
        return this.origin_message;
    }

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
    public int hashCode() {
        return Objects.hash(id, issued_at, duration, timeout, enqueued_at_olt, dequeued_at_olt, completed, origin_message, response);
    }
}
