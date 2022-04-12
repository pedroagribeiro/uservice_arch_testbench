package pt.producer.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Table(name = "responses")
public class Response {

    @Id
    @GeneratedValue(generator="response_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "response_id_seq", sequenceName = "response_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "status", nullable = false)
    private int status;

    @Column(name = "started_handling", nullable = false)
    private long started_handling;

    @Column(name = "ended_handling", nullable = false)
    private long ended_handling;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "origin_request_id", referencedColumnName = "id")
    private OltRequest origin_request;

    @Column(name = "timedout", nullable = false)
    private boolean timedout;

    public Response() {}

    public Response(final int request_id, final int status, final long started_handling, final long ended_handling) {
        this.status = status;
        this.started_handling = started_handling;
        this.ended_handling = ended_handling;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setOriginRequest(OltRequest request) {
        this.origin_request = request;
    }

    public OltRequest getOriginRequest() {
        return this.origin_request;
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
                "id=" + id +
                ", status=" + status +
                ", started_handling='" + started_handling + '\'' +
                ", ended_handling=" + ended_handling +
                ", origin_request=" + origin_request +
                ", timedout=" + timedout +
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
                origin_request.equals(response.origin_request) &&
                timedout == response.timedout
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                status,
                started_handling,
                ended_handling,
                timedout
        );
    }
}
