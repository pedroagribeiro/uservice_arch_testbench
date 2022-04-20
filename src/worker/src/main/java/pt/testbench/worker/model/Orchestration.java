package pt.testbench.worker.model;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class Orchestration {

    @Expose
    private int id;
    @Expose
    private int olts;
    @Expose
    private int messages;
    @Expose
    private int workers;
    @Expose
    private int algorithm;
    @Expose
    private int sequence;

    public Orchestration() {

    }

    public int getId() {
        return this.id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getOlts() {
        return this.olts;
    }

    public void setOlts(final int olts) {
        this.olts = olts;
    }

    public int getMessages() {
        return this.messages;
    }

    public void setMessages(final int messages) {
        this.messages = messages;
    }

    public int getWorkers() {
        return this.workers;
    }

    public void setWorkers(final int workers) {
        this.workers = workers;
    }

    public int getAlgorithm() {
        return this.algorithm;
    }

    public void setAlgorithm(final int algorithm) {
        this.algorithm = algorithm;
    }

    public int getSequence() {
        return this.sequence;
    }

    public void setSequence(final int sequence) { this.sequence = sequence; }

}
