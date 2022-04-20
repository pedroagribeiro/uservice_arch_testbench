package pt.testbench.worker.model;

import com.google.gson.annotations.Expose;
import org.hibernate.annotations.Proxy;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Table(name = "responses")
@Proxy(lazy = false)
public class Response {

    @Expose
    @Id
    private String id;

    @Expose
    @Column(name = "status")
    private int status;

    @Expose
    @Column(name = "request_enqueued_at_olt")
    private long requestEnqueuedAtOlt;

    @Expose
    @Column(name = "request_dequeued_at_olt")
    private long requestDequeuedAtOlt;

    @Expose
    @Column(name = "started_handling")
    private long startedHandling;

    @Expose
    @Column(name = "ended_handling")
    private long endedHandling;

    @Expose
    @Column(name = "timedout")
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
        this.requestEnqueuedAtOlt = request_enqueued_at_olt;
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

    /**
    @Override
    public String toString() {
        return "Reponse{" +
                "id=" + id + '\'' +
                ", status=" + status + '\'' +
                ", started_handling='" + started_handling + '\'' +
                ", ended_handling=" + ended_handling + '\'' +
                ", timedout=" + timedout + '\'' +
                ", origin_request=" + origin_request +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) o;
        return (
                id == response.id &&
                        status == response.status &&
                        started_handling == response.started_handling &&
                        ended_handling == response.ended_handling &&
                        timedout == response.timedout &&
                        Objects.equals(origin_request, response.origin_request)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                status,
                started_handling,
                ended_handling,
                timedout,
                origin_request
        );
    }
    **/
}
