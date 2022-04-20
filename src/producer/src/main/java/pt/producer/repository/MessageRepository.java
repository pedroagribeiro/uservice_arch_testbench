package pt.producer.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.producer.model.Message;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer> {

    @Query(value = "SELECT * FROM messages ORDER BY id DESC LIMIT 1", nativeQuery = true)
    List<Message> findMessageWithHighestId();

    @Query(value = "SELECT * FROM messages WHERE id >= :lower_boundary and id <= :upper_boundary", nativeQuery = true)
    List<Message> findMessagesBetweenIdRange(@Param("lower_boundary") int lower_boundary, @Param("upper_boundary") int upper_boundary);
}
