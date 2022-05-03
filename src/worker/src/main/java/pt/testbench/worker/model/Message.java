package pt.testbench.worker.model;

import com.google.gson.annotations.Expose;
import lombok.Data;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Table(name = "messages")
@Proxy(lazy = false)
public class Message {

    @Expose
    @Id
    @GeneratedValue(generator = "message_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "message_id_seq", sequenceName = "message_id_seq", allocationSize = 1)
    private int id;

    @Expose
    @Column(name = "olt", nullable = false)
    private String olt;

    @Expose
    @Column(name = "issued_at", nullable = false)
    private long issuedAt;

    @Expose
    @Column(name = "worker")
    private int worker;

    @Expose
    @Column(name = "started_processing")
    private long startedProcessing;

    @Expose
    @Column(name = "completed_processing")
    private long completedProcessing;

    @Expose
    @Column(name = "successful")
    private boolean successful;

    @Expose
    @OneToMany(mappedBy="originMessage", fetch = FetchType.EAGER)
    private List<OltRequest> oltRequests;

    @Expose
    @Column(name = "minimum_theoretical_duration")
    private long minimumTheoreticalDuration;

    @Expose
    @Column(name = "yellow_requests")
    private int yellowRequests;

    @Expose
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

    public void setOltRequests(List<OltRequest> olt_requests) {
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

    /**
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
    **/
}
