package pt.testbench.broker.model;

import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "messages")
@Proxy(lazy = false)
public class Message {

    @Id
    @GeneratedValue(generator = "message_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "message_id_seq", sequenceName = "message_id_seq", allocationSize = 1)
    private int id;

    @Column(name = "olt", nullable = false)
    private String olt;

    @Column(name = "issued_at", nullable = false)
    private long issuedAt;

    @Column(name = "worker")
    private int worker;

    @Column(name = "started_processing")
    private long startedProcessing;

    @Column(name = "completed_processing")
    private long completedProcessing;

    @Column(name = "successful")
    private boolean successful;

    @OneToMany(mappedBy="originMessage")
    private List<OltRequest> oltRequests;

    @Column(name = "minimum_theoretical_duration")
    private long minimumTheoreticalDuration;

    @Column(name = "yellow_requests")
    private int yellowRequests;

    @Column(name = "red_requests")
    private int redRequests;

    public Message(final String olt) {
        this.olt = olt;
        this.issuedAt = new Date().getTime();
        this.oltRequests = new ArrayList<>();
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
        this.issuedAt = issued_at;
    }

    public long getIssuedAt() {
        return this.issuedAt;
    }

    public void setWorker(final int worker) {
        this.worker = worker;
    }

    public int getWorker() {
        return this.worker;
    }

    public void setStartedProcessing(long startedProcessing) {
        this.startedProcessing = startedProcessing;
    }

    public long getStartedProcessing() {
        return this.startedProcessing;
    }

    public void setCompletedProcessing(final long completedProcessing) {
        this.completedProcessing = completedProcessing;
    }

    public long getCompletedProcessing() {
        return this.completedProcessing;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public boolean getSuccessful() {
        return this.successful;
    }

    public void setOltRequests(final List<OltRequest> olt_requests) {
        this.oltRequests = olt_requests;
    }

    public List<OltRequest> getOltRequests() {
        return this.oltRequests;
    }

    public void setMinimumTheoreticalDuration(final long minimum_theoretical_duration) {
        this.minimumTheoreticalDuration = minimum_theoretical_duration;
    }

    public long getMinimumTheoreticalDuration() {
        return this.minimumTheoreticalDuration;
    }

    public void setYellowRequests(int yellow_requests) {
        this.yellowRequests = yellow_requests;
    }

    public int getYellowRequests() {
        return this.yellowRequests;
    }

    public void setRedRequests(int red_requests) {
        this.redRequests = red_requests;
    }

    public int getRedRequests() {
        return this.redRequests;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", olt='" + olt + '\'' +
                ", issued_at=" + issuedAt + '\'' +
                ", worker=" + worker + '\'' +
                ", startProcessing= " + startedProcessing + '\'' +
                ", completedProcessing=" + completedProcessing + '\'' +
                ", successful= " + successful + '\'' +
                ", olt_requests=" + oltRequests + '\'' +
                ", minimumTheoreticalDuration=" + minimumTheoreticalDuration + '\'' +
                ", yellowRequests=" + yellowRequests + '\'' +
                ", redRequets=" + redRequests + 
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
                Objects.equals(issuedAt, message.issuedAt) &&
                Objects.equals(worker, message.worker) &&
                Objects.equals(startedProcessing, message.startedProcessing) &&
                Objects.equals(completedProcessing, message.completedProcessing) &&
                successful == message.successful &&
                Objects.equals(oltRequests, message.oltRequests) &&
                minimumTheoreticalDuration == message.minimumTheoreticalDuration &&
                yellowRequests == message.yellowRequests &&
                redRequests == message.redRequests
        );

    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                olt,
                issuedAt,
                startedProcessing,
                completedProcessing,
                successful,
                oltRequests,
                minimumTheoreticalDuration,
                yellowRequests,
                redRequests
        );
    }
}
