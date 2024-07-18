package org.jconfdominicana.config;

import io.quarkus.hibernate.orm.PersistenceUnitExtension;
import io.quarkus.hibernate.orm.runtime.tenant.TenantResolver;
import jakarta.inject.Inject;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jconfdominicana.model.common.Tenant;
import org.jconfdominicana.security.vaadin.CacheService;
//import org.jconfdominicana.services.VaadinServices;

@PersistenceUnitExtension
@Slf4j
//@Priority(1)
public class CurrentTenantResolver implements TenantResolver {

    @Inject
    CacheService cacheService;

    @Setter
    String principal;

    public static final String DEFAULT = "public";

    @Override
    public String getDefaultTenantId() {
        return DEFAULT;
    }

    @Override
    public String resolveTenantId() {
//        if (VaadinSession.getCurrent() != null) {
//            Tenant tenant = (Tenant) VaadinSession.getCurrent().getAttribute("tenant");
//            if (tenant != null) {
//                log.info("VIEW Schema: {}", tenant);
//                return tenant.getTenantId();
//            }
//        }
        String currentTenant = TenantContext.getCurrentTenant();
        if (currentTenant != null && !currentTenant.isEmpty()) {
            log.info("API schema: {}", currentTenant);
            return currentTenant;
        }

        if (principal != null && !principal.isEmpty()) {
            Tenant tenant = cacheService.getTenant(principal);
            if (tenant != null) {
                log.info("VIEW Schema: {}", tenant);
                return tenant.getTenantId();
            }
        }

        String defaultTenantId = getDefaultTenantId();
        log.info("Default schema: {}", defaultTenantId);
        return defaultTenantId;
    }


}
