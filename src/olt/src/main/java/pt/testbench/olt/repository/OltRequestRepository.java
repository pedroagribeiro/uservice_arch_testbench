package pt.testbench.olt.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pt.testbench.olt.model.OltRequest;

@Repository
public interface OltRequestRepository extends CrudRepository<OltRequest, String> {}
