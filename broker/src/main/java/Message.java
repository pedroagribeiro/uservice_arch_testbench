import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Message implements Serializable {

    private String olt;
    private int processing_time;
    private Date forwarded_by_broker;
    private Date handled_by_worker;

    public Message(final String olt, final int processing_time) {
        this.olt = olt;
        this.processing_time = processing_time;
        this.forwarded_by_broker = null;
        this.handled_by_worker = null;
    }

    public void set_olt(final String olt) {
        this.olt = olt;
    }

    public String get_olt() {
        return this.olt;
    }

    public void set_processing_time(final int processing_time) {
        this.processing_time = processing_time;
    }

    public int get_processing_time() {
        return this.processing_time;
    }

    public void set_forwarded_by_broker(final Date forwarded) {
        this.forwarded_by_broker = forwarded;
    }

    public Date get_forwarded_by_broker() {
        return this.forwarded_by_broker;
    }

    public void set_handled_by_worker(final Date handled) {
        this.handled_by_worker = handled;
    }

    public Date get_handled_by_worker() {
        return this.handled_by_worker;
    }

    @Override
    public String toString() {
        return "utils.Message{" +
                "olt='" + olt + '\'' +
                ", processing_time=" + processing_time +
                ", forwarded_by_broker=" + forwarded_by_broker +
                ", handled_by_worker=" + handled_by_worker +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return processing_time == message.processing_time && Objects.equals(olt, message.olt) && Objects.equals(forwarded_by_broker, message.forwarded_by_broker) && Objects.equals(handled_by_worker, message.handled_by_worker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(olt, processing_time, forwarded_by_broker, handled_by_worker);
    }
}
