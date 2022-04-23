package pt.producer.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.producer.model.OltRequest;
import pt.producer.model.Result;

import java.util.List;

@Repository
public interface ResultRepository extends CrudRepository<Result, Integer> {

    @Query(value = "SELECT * FROM results ORDER BY id DESC LIMIT 1", nativeQuery = true)
    List<Result> findResultWithHighestId();

    @Query(value = "SELECT * FROM results WHERE sequence = :sequence and workers = :workers and olts = :olts", nativeQuery = true)
    List<Result> findRequestsWithGivenSequenceWorkersOlts(@Param("sequence") int sequence, @Param("workers") int workers, @Param("olts") int olts);
}
