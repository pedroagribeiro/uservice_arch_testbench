package pt.testbench.broker.model;
import java.util.Objects;
import lombok.Data;

@Data
public class Response {

    private String id;
    private int status;
    private long requestEnqueuedAtOlt;
    private long requestDequeuedAtOlt;
    private long startedHandling;
    private long endedHandling;
    private boolean timedout;

    public Response() {}

    public Response(final int status, final long started_handling, final long ended_handling) {
        this.status = status;
        this.startedHandling = started_handling;
        this.endedHandling = ended_handling;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setStatus(final int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    public void setRequestEnqueuedAtOlt(final long request_enqueued_at_olt) {
        this.requestDequeuedAtOlt = request_enqueued_at_olt;
    }

    public long getRequestEnqueuedAtOlt() {
        return this.requestEnqueuedAtOlt;
    }

    public void setRequestDequeuedAtOlt(final long request_dequeued_at_olt) {
        this.requestDequeuedAtOlt = request_dequeued_at_olt;
    }

    public long getRequestDequeuedAtOlt() {
        return this.requestDequeuedAtOlt;
    }

    public void setStartedHandling(final long started_handling) {
        this.startedHandling = started_handling;
    }

    public long getStartedHandling() {
        return this.startedHandling;
    }

    public void setEndedHandling(final long ended_handling) {
        this.endedHandling = ended_handling;
    }

    public long getEndedHandling() {
        return this.endedHandling;
    }

    public void setTimedout(final boolean timedout) {
        this.timedout = timedout;
    }

    public boolean getTimedout() {
        return this.timedout;
    }

    @Override
    public String toString() {
        return "Reponse{" +
                "id=" + id + '\'' +
                ", status=" + status + '\'' +
                ", request_enqueued_at_olt=" + requestEnqueuedAtOlt + '\'' +
                ", request_dequeued_at_olt=" + requestDequeuedAtOlt + '\'' +
                ", started_handling='" + startedHandling + '\'' +
                ", ended_handling=" + endedHandling + '\'' +
                ", timedout=" + timedout + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) o;
        return (
                Objects.equals(id, response.id) &&
                status == response.status &&
                requestEnqueuedAtOlt == response.requestEnqueuedAtOlt &&
                requestDequeuedAtOlt == response.requestDequeuedAtOlt &&
                startedHandling == response.startedHandling &&
                endedHandling == response.endedHandling &&
                timedout == response.timedout
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                status,
                startedHandling,
                endedHandling,
                timedout
        );
    }
}
