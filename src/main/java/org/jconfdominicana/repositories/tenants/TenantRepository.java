package org.jconfdominicana.repositories.tenants;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;
import org.jconfdominicana.model.Tenant;

@Repository
public interface TenantRepository extends CrudRepository<Tenant, Long> {
}
