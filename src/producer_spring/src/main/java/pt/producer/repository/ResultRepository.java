package pt.producer.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pt.producer.model.Result;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResultRepository extends CrudRepository<Result, Integer> {

    List<Result> findAll();

    Optional<Result> findById(final int id);
}
