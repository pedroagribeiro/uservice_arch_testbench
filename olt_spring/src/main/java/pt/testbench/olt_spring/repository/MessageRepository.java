package pt.testbench.olt_spring.repository;

import org.springframework.data.repository.CrudRepository;
import pt.testbench.olt_spring.model.Message;

public interface MessageRepository extends CrudRepository<Message, Integer> {}
