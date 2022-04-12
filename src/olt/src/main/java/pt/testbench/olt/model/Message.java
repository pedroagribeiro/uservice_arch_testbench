package pt.testbench.olt.model;

import java.util.Date;
import java.util.Objects;

public class Message {

    private int id;
    private String olt;
    private long issued_at;
    private int worker;
    private long completed;
    private boolean successful;
    private long minimumTheoreticalDuration;
    private boolean hasRedRequests;


    public Message(final int id, final String olt) {
        this.id = id;
        this.olt = olt;
        this.issued_at = new Date().getTime();
        this.completed = -1;
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
        return "utils.Message{" +
                "id=" + id +
                ", olt='" + olt + '\'' +
                ", issued_at=" + issued_at +
                ", worker=" + worker +
                ", completed=" + completed +
                ", successful=" + successful +
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
                worker == message.worker &&
                completed == message.completed &&
                successful == message.successful
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                olt,
                issued_at,
                worker,
                completed,
                successful
        );
    }
}