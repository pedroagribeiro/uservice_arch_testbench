package pt.producer.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pt.producer.model.Result;

import java.util.List;

@Repository
public interface ResultRepository extends CrudRepository<Result, Integer> {

    @Query(value = "SELECT * FROM results ORDER BY id DESC LIMIT 1", nativeQuery = true)
    List<Result> findResultWithHighestId();
}
