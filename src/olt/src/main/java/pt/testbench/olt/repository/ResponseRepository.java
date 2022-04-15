package pt.testbench.olt.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pt.testbench.olt.model.Response;

@Repository
public interface ResponseRepository extends CrudRepository<Response, String> {}
