package pt.testbench.worker.repository;

import org.springframework.data.repository.CrudRepository;
import pt.testbench.worker.model.Response;

public interface ResponseRepository extends CrudRepository<Response, Integer> {}
