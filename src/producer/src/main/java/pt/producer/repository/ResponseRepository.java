package pt.producer.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pt.producer.model.Response;

@Repository
public interface ResponseRepository extends CrudRepository<Response, String> {}
