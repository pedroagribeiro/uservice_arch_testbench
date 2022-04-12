package pt.producer.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import pt.producer.model.OltRequest;

@Repository
public interface OltRequestRepository extends CrudRepository<OltRequest, Long> {

    @Query(value = "SELECT * FROM olt_requests WHERE origin_message_id >= :message_id_lower_boundary and origin_message_id <= :message_id_upper_boundary", nativeQuery = true)
    List<OltRequest> findRequestBetweenMessagesRange(@Param("message_id_lower_boundary") int message_id_lower_boundary, @Param("message_id_upper_boundary") int message_id_upper_boundary);
}
