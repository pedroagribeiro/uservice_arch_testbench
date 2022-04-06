package pt.testbench.worker_spring.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Table(name = "responses")
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "status", nullable = false)
    private int status;

    @Column(name = "started_handling", nullable = false)
    private long started_handling;

    @Column(name = "ended_handling", nullable = false)
    private long ended_handling;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "origin_message_id", referencedColumnName = "id")
    private Message origin_message;

    @Column(name = "timedout", nullable = false)
    private boolean timedout;

    public Response() {}

    public Response(final int request_id, final int status, final long started_handling, final long ended_handling, final Message origin_message) {
        this.status = status;
        this.started_handling = started_handling;
        this.ended_handling = ended_handling;
        this.origin_message = origin_message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void set_origin_message(final Message message) {
        this.origin_message = message;
    }

    public Message get_origin_message() {
        return this.origin_message;
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
                ", origin_message=" + origin_message.toString() +
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
                        origin_message == response.origin_message &&
                        timedout == response.timedout;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                status,
                started_handling,
                ended_handling,
                origin_message,
                timedout
        );
    }
}
