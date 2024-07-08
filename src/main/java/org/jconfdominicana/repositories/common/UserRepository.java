package org.jconfdominicana.repositories.common;

import jakarta.data.repository.*;
import org.jconfdominicana.model.common.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    @Find
    User findByUsername(String username);

}
