package pt.testbench.broker.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.Objects;

@Data
@Entity
@Table(name = "messages")
public class Message {

    @Id
    private int id;

    @Column(name = "olt", nullable = false)
    private String olt;

    @Column(name = "issued_at", nullable = false)
    private long issued_at;

    @Column(name = "worker", nullable = false)
    private int worker;

    @Column(name = "completed", nullable = false)
    private long completed;

    @Column(name = "successful", nullable = false)
    private boolean successful;

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

    @Override
    public String toString() {
        return "utils.Message{" +
                "id=" + id +
                ", olt='" + olt + '\'' +
                ", issued_at=" + issued_at + '\'' +
                ", worker=" + worker + + '\'' +
                ", completed=" + completed +
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
                Objects.equals(completed, message.completed)
       );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                olt,
                issued_at,
                worker,
                completed
        );
    }
}