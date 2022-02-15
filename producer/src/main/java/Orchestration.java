public class Orchestration {

    private int olts;
    private int messages;
    private boolean no_broker;
    private int workers;
    private int algorithm;

    public Orchestration() {

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

    public boolean get_no_broker() {
        return this.no_broker;
    }

    public void set_no_broker(final boolean no_broker) {
        this.no_broker = no_broker;
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
