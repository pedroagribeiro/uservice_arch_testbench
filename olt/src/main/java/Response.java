import java.io.Serializable;
import java.util.Objects;

public class Response implements Serializable {
    
    private int status;
    private long started_handling;
    private long ended_handling;

    public Response() {}

    public Response(final int status, final long started_handling, final long ended_handling) {
        this.status = status;
        this.started_handling = started_handling;
        this.ended_handling = ended_handling;
    }

    public void set_status(final int status) {
        this.status = status;
    }

    public int get_status() {
        return this.status;
    }

    public void set_started_handling(final long started_handling) {
        this.started_handling = started_handling;
    }

    public long get_started_handling() {
        return this.started_handling;
    }

    public void set_ended_handling(final long ended_handling) {
        this.ended_handling = ended_handling;
    }
    
    public long get_ended_handling() {
        return this.ended_handling;
    }

    @Override
    public String toString() {
        return "Reponse{" +
                "status=" + status +
                ", started_handling='" + started_handling + '\'' +
                ", ended_handling=" + ended_handling +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) o;
        return
            status == response.status &&
            started_handling == response.started_handling &&
            ended_handling == response.ended_handling; 
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                status,
                started_handling,
                ended_handling
        );
    }
}
