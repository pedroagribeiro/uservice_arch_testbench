package pt.producer.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.producer.model.Response;

import java.util.List;

@Repository
public interface ResponseRepository extends CrudRepository<Response, String> {

    @Query(value ="SELECT * FROM responses WHERE origin_message_id >= :lower_bound and origin_message_id <= :upper_bound", nativeQuery = true)
    List<Response> findAllResponsesBetweenGivenIds(@Param("lower_bound") int lower_bound, @Param("upper_bound") int upper_bound);
}
