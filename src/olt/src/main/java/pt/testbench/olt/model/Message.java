package pt.testbench.olt.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(generator = "message_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "message_id_seq", sequenceName = "message_id_seq", allocationSize = 1)
    private int id;

    @Column(name = "olt", nullable = false)
    private String olt;

    @Column(name = "issued_at", nullable = false)
    private long issued_at;

    @Column(name = "worker")
    private int worker;

    @Column(name = "completed")
    private long completed;

    @Column(name = "successful")
    private boolean successful;

    @OneToMany(mappedBy="origin_message")
    private Set<OltRequest> olt_requests;

    @Column(name = "minimum_theoretical_duration")
    private long minimumTheoreticalDuration;

    @Column(name = "has_red_requests")
    private boolean hasRedRequests;

    public Message(final String olt) {
        this.olt = olt;
        this.issued_at = new Date().getTime();
    }

    public Message() {

    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setOlt(final String olt) {
        this.olt = olt;
    }

    public String getOlt() {
        return this.olt;
    }

    public void setIssuedAt(final long issued_at) {
        this.issued_at = issued_at;
    }

    public long getIssuedAt() {
        return this.issued_at;
    }

    public void setWorker(final int worker) {
        this.worker = worker;
    }

    public int getWorker() {
        return this.worker;
    }

    public void setCompleted(final long completed) {
        this.completed = completed;
    }

    public long getCompleted() {
        return this.completed;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public boolean getSuccessful() {
        return this.successful;
    }

    public Set<OltRequest> getOltRequests() {
        return this.olt_requests;
    }

    public void setMinimumTheoreticalDuration(final long minimumTheoreticalDuration) {
        this.minimumTheoreticalDuration = minimumTheoreticalDuration;
    }

    public long getMinimumTheoreticalDuration() {
        return this.minimumTheoreticalDuration;
    }

    public void setHasRedRequests(final boolean hasRedRequests) {
        this.hasRedRequests = hasRedRequests;
    }

    public boolean getHasRedRequests() {
        return this.hasRedRequests;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", olt='" + olt + '\'' +
                ", issued_at=" + issued_at + '\'' +
                ", worker=" + worker + '\'' +
                ", completed=" + completed + '\'' +
                ", successful= " + successful + '\'' +
                ", olt_requests=" + olt_requests + '\'' +
                ", minimumTheoreticalDuration=" + minimumTheoreticalDuration + '\'' +
                ", hasRedRequests= " + hasRedRequests +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return (
                id == message.id &&
                        Objects.equals(olt, message.olt) &&
                        Objects.equals(issued_at, message.issued_at) &&
                        Objects.equals(worker, message.worker) &&
                        Objects.equals(completed, message.completed) &&
                        successful == message.successful &&
                        Objects.equals(olt_requests, message.olt_requests) &&
                        minimumTheoreticalDuration == message.minimumTheoreticalDuration &&
                        hasRedRequests == message.hasRedRequests
        );

    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                olt,
                issued_at,
                completed,
                successful,
                olt_requests,
                minimumTheoreticalDuration,
                hasRedRequests
        );
    }
}
