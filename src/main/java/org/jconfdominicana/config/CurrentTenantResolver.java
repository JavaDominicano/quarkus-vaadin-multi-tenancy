package org.jconfdominicana.config;

import io.quarkus.hibernate.orm.PersistenceUnitExtension;
import io.quarkus.hibernate.orm.runtime.tenant.TenantResolver;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.jconfdominicana.model.common.Tenant;
import org.jconfdominicana.security.vaadin.CacheService;
import org.jconfdominicana.security.vaadin.SecurityService;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@PersistenceUnitExtension
@RequestScoped
@Slf4j
public class CurrentTenantResolver implements TenantResolver {

    @Inject
    CacheService cacheService;
    @Inject
    SecurityService securityService;

    public static final String DEFAULT = "public";

    @Override
    public String getDefaultTenantId() {
        return DEFAULT;
    }

    @Override
    public String resolveTenantId() {
        Optional<String> optional = securityService.getUsername();
        if (optional.isPresent()) {
            try {
                Tenant tenant = cacheService.getTenant(optional.get());
                if (tenant != null) {
                    log.info("Schema: {}", tenant);
                    return tenant.getTenantId();
                }

            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        String defaultTenantId = getDefaultTenantId();
        log.info("Default schema: {}", defaultTenantId);
        return defaultTenantId;
    }
}
