package pt.testbench.worker.model;

import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "responses")
@Proxy(lazy = false)
public class Response {

    @Id
    private String id;

    @Column(name = "status")
    private int status;

    @Column(name = "started_handling")
    private long started_handling;

    @Column(name = "ended_handling")
    private long ended_handling;

    @Column(name = "timedout")
    private boolean timedout;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_request_id", referencedColumnName = "id")
    private OltRequest origin_request;

    public Response() {}

    public Response(final int status, final long started_handling, final long ended_handling) {
        this.status = status;
        this.started_handling = started_handling;
        this.ended_handling = ended_handling;
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

    public void setStartedHandling(final long started_handling) {
        this.started_handling = started_handling;
    }

    public long getStartedHandling() {
        return this.started_handling;
    }

    public void setEndedHandling(final long ended_handling) {
        this.ended_handling = ended_handling;
    }

    public long getEndedHandling() {
        return this.ended_handling;
    }

    public void setTimedout(final boolean timedout) {
        this.timedout = timedout;
    }

    public boolean getTimedout() {
        return this.timedout;
    }

    public OltRequest getOriginRequest() {
        return this.origin_request;
    }

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
}
