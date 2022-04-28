package pt.producer.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.producer.model.OltRequest;

import java.util.List;

@Repository
public interface OltRequestRepository extends CrudRepository<OltRequest, String> {

    @Query(value = "SELECT * FROM olt_requests WHERE origin_message_id >= :lower_boundary and origin_message_id <= :upper_boundary", nativeQuery = true)
    List<OltRequest> findRequestsBetweenMessagesRange(@Param("lower_boundary") int lower_boundary, @Param("upper_boundary") int upper_boundary);
}
