package org.jconfdominicana.repositories;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Find;
import jakarta.data.repository.Repository;
import org.jconfdominicana.model.Profile;

@Repository
public interface ProfileRepository extends CrudRepository<Profile, Long> {

    @Find
    Profile findByUsername(String username);
}
