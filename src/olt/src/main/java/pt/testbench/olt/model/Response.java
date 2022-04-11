package pt.testbench.olt.model;

import java.util.Objects;

public class Response {

    private int id;

    private int status;

    private long started_handling;

    private long ended_handling;

    private OltRequest origin_request;

    private boolean timedout;

    public Response() {}

    public Response(final int status, final long started_handling, final long ended_handling, final OltRequest origin_request) {
        this.status = status;
        this.started_handling = started_handling;
        this.ended_handling = ended_handling;
        this.origin_request = origin_request;
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

    public void setOriginRequest(final OltRequest request) {
        this.origin_request = origin_request;
    }

    public OltRequest getOriginRequest() {
        return this.origin_request;
    }

    public void set_timedout(final boolean timedout) {
        this.timedout = timedout;
    }

    public boolean get_timedout() {
        return this.timedout;
    }

    @Override
    public String toString() {
        return "Reponse{" +
                "status=" + status +
                ", started_handling='" + started_handling + '\'' +
                ", ended_handling=" + ended_handling +
                ", origin_request=" + origin_request.toString() +
                ", timedout=" + timedout +
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
                        ended_handling == response.ended_handling &&
                        origin_request == response.origin_request &&
                        timedout == response.timedout;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                status,
                started_handling,
                ended_handling,
                origin_request,
                timedout
        );
    }
}