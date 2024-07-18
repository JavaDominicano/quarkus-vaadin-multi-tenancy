package org.jconfdominicana.repositories.common;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Find;
import jakarta.data.repository.Repository;
import org.jconfdominicana.model.common.Tenant;

import java.util.Optional;

@Repository
public interface TenantRepository extends CrudRepository<Tenant, String> {

//    @Find
//    Optional<Tenant> findById(String id);
}
