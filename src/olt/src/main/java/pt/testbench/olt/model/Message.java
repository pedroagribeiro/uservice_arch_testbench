package pt.testbench.olt.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
public class Message {

    private int id;
    private String olt;
    private long issuedAt;
    private int worker;

    private long startedProcessing;

    private long completedProcessing;
    private boolean successful;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<OltRequest> oltRequests;

    private long minimum_theoretical_duration;

    private boolean has_red_requests;

    public Message(final String olt) {
        this.olt = olt;
        this.issuedAt = new Date().getTime();
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
        this.minimum_theoretical_duration = minimum_theoretical_duration;
    }

    public long getMinimumTheoreticalDuration() {
        return this.minimum_theoretical_duration;
    }

    public void setHasRedRequests(final boolean has_red_requests) {
        this.has_red_requests = has_red_requests;
    }

    public boolean getHasRedRequests() {
        return this.has_red_requests;
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
