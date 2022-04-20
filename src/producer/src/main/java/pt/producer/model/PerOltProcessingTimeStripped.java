package pt.producer.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

public class PerOltProcessingTimeStripped {

    private int id;
    private String olt;
    private long minimumProcessingTime;
    private long maximumProcessingTime;
    private double averageProcessingTime;

    public PerOltProcessingTimeStripped() {}

    public PerOltProcessingTimeStripped(PerOltProcessingTime perOltProcessingTime) {
        this.olt = perOltProcessingTime.getOlt();
        this.minimumProcessingTime = perOltProcessingTime.getMinimumProcessingTime();
        this.maximumProcessingTime = perOltProcessingTime.getMaximumProcessingTime();
        this.averageProcessingTime = perOltProcessingTime.getAverageProcessingTime();
    }

    public String getOlt() {
        return olt;
    }

    public void setOlt(String olt) {
        this.olt = olt;
    }

    public long getMinimumProcessingTime() {
        return minimumProcessingTime;
    }

    public void setMinimumProcessingTime(long minimumProcessingTime) {
        this.minimumProcessingTime = minimumProcessingTime;
    }

    public long getMaximumProcessingTime() {
        return maximumProcessingTime;
    }

    public void setMaximumProcessingTime(long maximumProcessingTime) {
        this.maximumProcessingTime = maximumProcessingTime;
    }

    public double getAverageProcessingTime() {
        return averageProcessingTime;
    }

    public void setAverageProcessingTime(double averageProcessingTime) {
        this.averageProcessingTime = averageProcessingTime;
    }

    @Override
    public String toString() {
        return "PerOltProcessingTime{" +
                "id=" + id +
                ", olt='" + olt + '\'' +
                ", minimumProcessingTime=" + minimumProcessingTime +
                ", maximumProcessingTime=" + maximumProcessingTime +
                ", averageProcessingTime=" + averageProcessingTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PerOltProcessingTimeStripped that = (PerOltProcessingTimeStripped) o;
        return id == that.id && minimumProcessingTime == that.minimumProcessingTime && maximumProcessingTime == that.maximumProcessingTime && Double.compare(that.averageProcessingTime, averageProcessingTime) == 0 && Objects.equals(olt, that.olt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, olt, minimumProcessingTime, maximumProcessingTime, averageProcessingTime);
    }
}
