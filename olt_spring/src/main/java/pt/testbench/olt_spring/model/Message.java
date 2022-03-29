package pt.testbench.olt_spring.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    private Integer id;

    @Column(name = "olt", nullable = false)
    private String olt;

    @Column(name = "processing_time", nullable = false)
    private long processing_time;

    @Column(name = "timeout", nullable = false)
    private long timeout;

    @Column(name = "issued_at", nullable = false)
    private long issued_at;

    @Column(name = "worker", nullable = false)
    private int worker;

    @Column(name = "enqueued_at_broker", nullable = false)
    private long enqueued_at_broker;

    @Column(name = "dequeued_at_broker", nullable = false)
    private long dequeued_at_broker;

    @Column(name = "enqueued_at_worker", nullable = false)
    private long enqueued_at_worker;

    @Column(name = "dequeued_at_worker", nullable = false)
    private long dequeued_at_worker;

    @Column(name = "enqueued_at_olt", nullable = false)
    private long enqueued_at_olt;

    @Column(name = "dequeued_at_olt", nullable = false)
    private long dequeued_at_olt;

    @Column(name = "completed", nullable = false)
    private long completed;

    public Message(final int id, final String olt, final long processing_time, final long timeout) {
        this.id = id;
        this.olt = olt;
        this.processing_time = processing_time;
        this.timeout = timeout;
        this.issued_at = new Date().getTime();
        this.enqueued_at_broker = -1;
        this.dequeued_at_broker = -1;
        this.enqueued_at_worker = -1;
        this.dequeued_at_worker = -1;
        this.enqueued_at_olt = -1;
        this.dequeued_at_olt = -1;
        this.completed = -1;
    }

    public Message() {

    }

    public void set_id(final int id) {
        this.id = id;
    }

    public int get_id() {
        return this.id;
    }

    public void set_olt(final String olt) {
        this.olt = olt;
    }

    public String get_olt() {
        return this.olt;
    }

    public void set_issued_at(final long issued_at) {
        this.issued_at = issued_at;
    }

    public long get_issued_at() {
        return this.issued_at;
    }

    public void set_worker(final int worker) {
        this.worker = worker;
    }

    public int get_worker() {
        return this.worker;
    }

    public void set_processing_time(final long processing_time) {
        this.processing_time = processing_time;
    }

    public long get_processing_time() {
        return this.processing_time;
    }

    public void set_timeout(final long timeout) {
        this.timeout = timeout;
    }

    public long get_timeout() {
        return this.timeout;
    }

    public void set_enqueued_at_broker(final long enqueued_at_broker) {
        this.enqueued_at_broker = enqueued_at_broker;
    }

    public long get_enqueued_at_broker() {
        return this.enqueued_at_broker;
    }

    public void set_dequeued_at_broker(final long dequeued_at_broker) {
        this.dequeued_at_broker = dequeued_at_broker;
    }

    public long get_dequeued_at_broker() {
        return this.dequeued_at_broker;
    }

    public void set_enqueued_at_worker(final long enqueued_at_worker) {
        this.enqueued_at_worker = enqueued_at_worker;
    }

    public long get_enqueued_at_worker() {
        return this.enqueued_at_worker;
    }

    public void set_dequeued_at_worker(final long dequeued_at_worker) {
        this.dequeued_at_worker = dequeued_at_worker;
    }

    public long get_dequeued_at_worker() {
        return this.dequeued_at_worker;
    }

    public void set_enqueued_at_olt(final long enqueued_at_olt) {
        this.enqueued_at_olt = enqueued_at_olt;
    }

    public long get_enqueued_at_olt() {
        return this.enqueued_at_olt;
    }

    public void set_dequeued_at_olt(final long dequeued_at_olt) {
        this.dequeued_at_olt = dequeued_at_olt;
    }

    public long get_dequeued_at_olt() {
        return this.dequeued_at_olt;
    }

    public void set_completed(final long completed) {
        this.completed = completed;
    }

    public long get_completed() {
        return this.completed;
    }

    @Override
    public String toString() {
        return "utils.Message{" +
                "id=" + id +
                ", olt='" + olt + '\'' +
                ", processing_time=" + processing_time +
                ", timeout=" + timeout +
                ", issued_at=" + issued_at +
                ", enqueued_at_broker=" + enqueued_at_broker +
                ", dequeued_at_broker=" + dequeued_at_broker +
                ", enqueued_at_worker=" + enqueued_at_worker +
                ", dequeued_at_worker=" + dequeued_at_worker +
                ", enqueued_at_olt=" + enqueued_at_olt +
                ", dequeued_at_olt=" + dequeued_at_olt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return
                id == message.id && processing_time == message.processing_time && timeout == message.timeout &&
                        Objects.equals(olt, message.olt) && Objects.equals(issued_at, message.issued_at) &&
                        Objects.equals(enqueued_at_broker, message.enqueued_at_broker) &&
                        Objects.equals(dequeued_at_broker, message.dequeued_at_broker) &&
                        Objects.equals(enqueued_at_worker, message.enqueued_at_worker) &&
                        Objects.equals(dequeued_at_worker, message.dequeued_at_worker) &&
                        Objects.equals(enqueued_at_olt, message.enqueued_at_olt) &&
                        Objects.equals(dequeued_at_olt, message.dequeued_at_olt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                olt,
                processing_time,
                timeout,
                issued_at,
                enqueued_at_broker,
                dequeued_at_broker,
                enqueued_at_worker,
                dequeued_at_worker,
                enqueued_at_olt,
                dequeued_at_olt,
                completed
        );
    }
}