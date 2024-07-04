package org.jconfdominicana.repositories.common;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;
import org.jconfdominicana.model.common.Tenant;

@Repository
public interface TenantRepository extends CrudRepository<Tenant, String> {
}
