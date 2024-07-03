package org.jconfdominicana.repositories.common;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Find;
import jakarta.data.repository.Repository;
import org.jconfdominicana.model.common.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    @Find
    User findByUsername(String username);
}
