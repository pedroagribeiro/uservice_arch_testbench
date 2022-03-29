package pt.testbench.worker_spring.repository;

import org.springframework.data.repository.CrudRepository;
import pt.testbench.worker_spring.model.Response;

public interface ResponseRepository extends CrudRepository<Response, Integer> {}
