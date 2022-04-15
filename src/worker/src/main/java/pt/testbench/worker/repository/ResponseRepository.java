package pt.testbench.worker.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pt.testbench.worker.model.Response;

@Repository
public interface ResponseRepository extends CrudRepository<Response, String> {}
