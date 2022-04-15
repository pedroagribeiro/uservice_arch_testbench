package pt.testbench.worker.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pt.testbench.worker.model.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer> {}
