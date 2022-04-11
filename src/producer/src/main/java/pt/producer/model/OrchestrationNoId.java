package pt.producer.model;

import lombok.Data;

@Data
public class OrchestrationNoId {

    private int olts;
    private int messages;
    private int workers;
    private int algorithm;

    public OrchestrationNoId() {

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

}
