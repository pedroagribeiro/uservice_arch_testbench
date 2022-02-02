import java.util.Objects;

public class Message {

    private String olt;
    private int processing_time;

    public Message(final String olt, final int processing_time) {
        this.olt = olt;
        this.processing_time = processing_time;
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

    @Override
    public String toString() {
        return "Message{" +
                "olt='" + olt + '\'' +
                ", processing_time=" + processing_time +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return processing_time == message.processing_time && olt.compareTo(message.olt) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(olt, processing_time);
    }
}
