package pt.producer.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import pt.producer.model.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer> {

   @Query(value = "SELECT * FROM messages WHERE id >= :message_id_lower_boundary and id <= :message_id_upper_boundary", nativeQuery = true) 
   List<Message> findMessageBetweenIdRange(@Param("message_id_lower_boundary") int message_id_lower_boundary, @Param("message_id_upper_boundary") int message_id_upper_boundary); 
}
