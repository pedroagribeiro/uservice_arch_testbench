package pt.producer.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pt.producer.model.Message;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer> {

    @Query(value = "SELECT * FROM messages ORDER BY id DESC LIMIT 1", nativeQuery = true)
    List<Message> findMessageWithHighestId();
}
