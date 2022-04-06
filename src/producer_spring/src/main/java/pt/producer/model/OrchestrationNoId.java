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

    public int get_olts() {
        return this.olts;
    }

    public void set_olts(final int olts) {
        this.olts = olts;
    }

    public int get_messages() {
        return this.messages;
    }

    public void set_messages(final int messages) {
        this.messages = messages;
    }

    public int get_workers() {
        return this.workers;
    }

    public void set_workers(final int workers) {
        this.workers = workers;
    }

    public int get_algorithm() {
        return this.algorithm;
    }

    public void set_algorithm(final int algorithm) {
        this.algorithm = algorithm;
    }

}
