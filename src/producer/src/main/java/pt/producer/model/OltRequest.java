package pt.producer.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;
import java.util.List;

@Data
@Entity
@Table(name = "olt_requests")
public class OltRequest {

    @Id
    @GeneratedValue(generator = "olt_request_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "olt_request_id_seq", sequenceName = "olt_request_id_seq", allocationSize = 1)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "origin_message_id", referencedColumnName = "id")
    private Message origin_message;

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

    @OneToOne(mappedBy = "origin_request")
    private Response response;

    public OltRequest() {

    }

    public OltRequest(final Message origin_message, final long duration, final long timeout) {
       this.origin_message = origin_message;
       this.duration = duration;
       this.timeout = timeout;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Message getOriginMessage() {
        return origin_message;
    }

    public void setOriginMessage(Message origin_message) {
        this.origin_message = origin_message;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OltRequest that = (OltRequest) o;
        return (
                id == that.id &&
                origin_message == that.origin_message &&
                issued_at == that.issued_at &&
                duration == that.duration &&
                timeout == that.timeout &&
                enqueued_at_olt == that.enqueued_at_olt &&
                dequeued_at_olt == that.dequeued_at_olt &&
                completed == that.completed
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, origin_message, issued_at, duration, timeout, enqueued_at_olt, dequeued_at_olt, completed);
    }
}
