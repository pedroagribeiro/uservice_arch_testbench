package pt.producer.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pt.producer.model.OltRequest;

@Repository
public interface OltRequestRepository extends CrudRepository<OltRequest, String> {}
