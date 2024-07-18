package org.jconfdominicana.config;

import com.vaadin.flow.server.VaadinSession;
import io.quarkus.hibernate.orm.PersistenceUnitExtension;
import io.quarkus.hibernate.orm.runtime.tenant.TenantResolver;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jconfdominicana.model.common.Tenant;
import org.jconfdominicana.security.vaadin.CacheService;

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
        if (VaadinSession.getCurrent() != null) {
            System.out.println(VaadinSession.getCurrent().getSession());
        }
        String currentTenant = TenantContext.getCurrentTenant();
        if (currentTenant != null && !currentTenant.isEmpty()) {
            return currentTenant;
        }

        if (principal != null && !principal.isEmpty()) {
            Tenant tenant = cacheService.getTenant(principal);
            if (tenant != null) {
                log.info("Schema: {}", tenant);
                return tenant.getTenantId();
            }
        }

        String defaultTenantId = getDefaultTenantId();
        log.info("Default schema: {}", defaultTenantId);
        return defaultTenantId;
    }


}
