package pt.producer.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.producer.model.PerOltProcessingTime;

import java.util.List;

@Repository
public interface PerOltProcessingTimeRepository extends CrudRepository<PerOltProcessingTime, Integer> {

    @Query(value = "SELECT * FROM per_olt_processing_times WHERE run_id = :current_run_id", nativeQuery = true)
    List<PerOltProcessingTime> findByRunId(@Param("current_run_id") int current_run_id);
}
