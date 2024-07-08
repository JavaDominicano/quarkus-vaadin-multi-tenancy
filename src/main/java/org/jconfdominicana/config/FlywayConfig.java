package org.jconfdominicana.config;


import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@ApplicationScoped
@RequiredArgsConstructor
public class FlywayConfig {

    //    @ConfigProperty(name = "application.flyway.migrate-on-startup")
    boolean migrateOnStartup = true;

    private final FlywayService flywayService;
//    private final TenantService tenantService;

    public void migrateFlyway(@Observes StartupEvent startupEvent) {
        if (migrateOnStartup) {
            migrateFlyway();
        }
    }

    public void migrateFlyway() {
        flywayService.initMetadataSchema();

        Arrays.asList("tenant_01", "tenant_02", "tenant_03")
                .forEach(flywayService::initNewTenantSchema);

//        for (Tenant tenant : tenantService.findAll()) {
//            flywayService.initNewTenantSchema(tenant.getTenantId());
//        }

    }

}
