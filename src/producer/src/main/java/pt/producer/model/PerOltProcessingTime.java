package pt.producer.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "per_olt_processing_times")
public class PerOltProcessingTime {

    @Id
    @GeneratedValue(generator = "per_olt_processing_time_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "per_olt_processing_time_id_seq", sequenceName = "per_olt_processing_time_id_seq", allocationSize = 1)
    private int id;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "run_id", referencedColumnName = "id")
    private Result concerningResult;

    @Column(name = "olt", nullable = false)
    private String olt;

    @Column(name = "minimum_processing_time", nullable = false)
    private long minimumProcessingTime;

    @Column(name = "maximum_processing_time", nullable = false)
    private long maximumProcessingTime;

    @Column(name = "average_processing_time", nullable = false)
    private double averageProcessingTime;

    public PerOltProcessingTime() {}

    public PerOltProcessingTime(Result concerningResult, String olt, long minimumProcessingTime, long maximumProcessingTime, double averageProcessingTime) {
        this.olt = olt;
        this.concerningResult = concerningResult;
        this.minimumProcessingTime = minimumProcessingTime;
        this.maximumProcessingTime = maximumProcessingTime;
        this.averageProcessingTime = averageProcessingTime;
    }

    public Result getConcerningResult() {
        return concerningResult;
    }

    public void setConcerningResult(Result concerningResult) {
        this.concerningResult = concerningResult;
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
                ", concerningResult=" + concerningResult +
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
        PerOltProcessingTime that = (PerOltProcessingTime) o;
        return id == that.id && minimumProcessingTime == that.minimumProcessingTime && maximumProcessingTime == that.maximumProcessingTime && Double.compare(that.averageProcessingTime, averageProcessingTime) == 0 && Objects.equals(concerningResult, that.concerningResult) && Objects.equals(olt, that.olt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, concerningResult, olt, minimumProcessingTime, maximumProcessingTime, averageProcessingTime);
    }
}
