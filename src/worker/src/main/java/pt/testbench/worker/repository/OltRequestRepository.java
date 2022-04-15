package pt.testbench.worker.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pt.testbench.worker.model.OltRequest;

@Repository
public interface OltRequestRepository extends CrudRepository<OltRequest, String> { }
