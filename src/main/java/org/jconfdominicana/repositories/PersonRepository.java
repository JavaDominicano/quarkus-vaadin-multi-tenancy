package org.jconfdominicana.repositories;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;
import org.jconfdominicana.model.Person;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {


}
