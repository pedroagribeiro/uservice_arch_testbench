package pt.testbench.worker.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import pt.testbench.worker.model.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer> {}
