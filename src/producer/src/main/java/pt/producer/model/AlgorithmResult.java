package pt.producer.model;

import java.util.List;
public class AlgorithmResult {

    private int algorithm;
    private List<Double> xx;
    private List<Double> yy;

    public AlgorithmResult(int algorithm, List<Double> xx, List<Double> yy) {
         this.algorithm = algorithm;
         this.xx = xx;
         this.yy = yy;
    }

    public int getAlgorithm() {
        return this.algorithm;
    }

    public void setAlgorithm(int algorithm) {
        this.algorithm = algorithm;
    }

    public List<Double> getXX() {
        return this.xx;
    }

    public void setXX(List<Double> xx) {
        this.xx = xx;
    }

    public List<Double> getYY() {
        return this.yy;
    }

    public void setYY(List<Double> yy) {
        this.yy = yy;
    }

}

