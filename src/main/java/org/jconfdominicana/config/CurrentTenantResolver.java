package org.jconfdominicana.config;

import io.quarkus.hibernate.orm.PersistenceUnitExtension;
import io.quarkus.hibernate.orm.runtime.tenant.TenantResolver;
import jakarta.enterprise.context.RequestScoped;
import lombok.extern.slf4j.Slf4j;

@PersistenceUnitExtension
@RequestScoped
@Slf4j
public class CurrentTenantResolver implements TenantResolver {

    @Override
    public String getDefaultTenantId() {
        return "public";
    }

    @Override
    public String resolveTenantId() {
        String tenant = TenantContext.getCurrentTenant();
        if (tenant != null && !tenant.isEmpty()) {
            log.info("Schema: {}", tenant);
            return tenant;
        }

        String defaultTenantId = getDefaultTenantId();
        log.info("Schema: {}", defaultTenantId);
        return defaultTenantId;
    }
}
