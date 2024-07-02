package org.jconfdominicana.repositories.tenants;

import jakarta.data.repository.CrudRepository;
import org.jconfdominicana.model.Tenant;

public interface TenantRepository extends CrudRepository<Tenant, Long> {
}
