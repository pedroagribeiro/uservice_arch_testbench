package pt.producer.model;

import lombok.Data;

@Data
public class Orchestration {

    private int id;
    private int olts;
    private int messages;
    private int workers;
    private int algorithm;

    public Orchestration() {

    }

    public Orchestration(int id, OrchestrationNoId orchestrationNoId) {
        this.id = id;
        this.olts = orchestrationNoId.getOlts();
        this.messages = orchestrationNoId.getMessages();
        this.workers = orchestrationNoId.getWorkers();
        this.algorithm = orchestrationNoId.getAlgorithm();
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

}
