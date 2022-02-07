import java.io.Serializable;
import java.util.Objects;

public class Message implements Serializable {

    private int id;
    private String olt;
    private int processing_time;
    private long issued_at;
    private long forwarded_by_broker;
    private long handled_by_worker;

    public Message(final int id, final String olt, final int processing_time) {
        this.id = id;
        this.olt = olt;
        this.processing_time = processing_time;
        this.issued_at = -1;
        this.forwarded_by_broker = -1;
        this.handled_by_worker = -1;
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

    public void set_processing_time(final int processing_time) {
        this.processing_time = processing_time;
    }

    public int get_processing_time() {
        return this.processing_time;
    }

    public void set_forwarded_by_broker(final long forwarded) {
        this.forwarded_by_broker = forwarded;
    }

    public long get_forwarded_by_broker() {
        return this.forwarded_by_broker;
    }

    public void set_handled_by_worker(final long handled) {
        this.handled_by_worker = handled;
    }

    public long get_handled_by_worker() {
        return this.handled_by_worker;
    }

    @Override
    public String toString() {
        return "utils.Message{" +
                "id=" + id +
                "olt='" + olt + '\'' +
                ", processing_time=" + processing_time +
                ", issued_at=" + issued_at +
                ", forwarded_by_broker=" + forwarded_by_broker +
                ", handled_by_worker=" + handled_by_worker +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return id == message.id && processing_time == message.processing_time && Objects.equals(olt, message.olt) && Objects.equals(issued_at, message.issued_at) && Objects.equals(forwarded_by_broker, message.forwarded_by_broker) && Objects.equals(handled_by_worker, message.handled_by_worker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, olt, processing_time, issued_at, forwarded_by_broker, handled_by_worker);
    }
}
