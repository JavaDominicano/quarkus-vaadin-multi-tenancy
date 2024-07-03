package org.jconfdominicana.config;

import io.quarkus.hibernate.orm.PersistenceUnitExtension;
import io.quarkus.hibernate.orm.runtime.tenant.TenantResolver;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@PersistenceUnitExtension
@RequestScoped
public class CustomTenantResolver  implements TenantResolver {

    @Inject
    RoutingContext context;



    @Override
    public String getDefaultTenantId() {
        return "base";
    }

    @Override
    public String resolveTenantId() {
        String path = context.request().path();
        String[] parts = path.split("/");

        if (parts.length == 0) {
            return getDefaultTenantId();
        }
        return parts[1];
    }
}
