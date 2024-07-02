package org.jconfdominicana.repositories.users;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;
import org.jconfdominicana.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
