package pt.testbench.broker_spring.model;

import java.util.HashMap;
import java.util.Map;

public class Status {

    // ARCHITECTURES
    // 1 -> BASE
    // 2 -> BLOQUEANTE
    // 3 -> HASH BASED
    // 4 -> DINÂMICO

    private int architecture;
    private int workers;
    private int olts;
    private boolean containerized;
    private Map<String, Integer> oracle;
    private int last_chosen_worker;

    public Status() {
        this.architecture = 1;
        this.workers = 1;
        this.olts = 1;
        this.containerized = false;
        this.oracle = new HashMap<>();
        this.last_chosen_worker = 0;
    }

    public int getArchitecture() {
        return architecture;
    }

    public int getWorkers() {
        return workers;
    }

    public int getOlts() {
        return olts;
    }

    public boolean isContainerized() {
        return containerized;
    }

    public Map<String, Integer> getOracle() {
        return oracle;
    }

    public int getLastChosenWorker() {
        return last_chosen_worker;
    }

    public void setArchitecture(int architecture) {
        this.architecture = architecture;
    }

    public void setWorkers(int workers) {
        this.workers = workers;
    }

    public void setOlts(int olts) {
        this.olts = olts;
    }

    public void setContainerized(boolean containerized) {
        this.containerized = containerized;
    }

    public void setOracle(Map<String, Integer> oracle) {
        this.oracle = oracle;
    }

    public void setLastChosenWorker(int last_chosen_worker) {
        this.last_chosen_worker = last_chosen_worker;
    }

}
