package org.jconfdominicana.config;


//import io.quarkus.runtime.StartupEvent;
//import jakarta.annotation.PostConstruct;
//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.enterprise.event.Observes;
//import lombok.RequiredArgsConstructor;
//
//@ApplicationScoped
//@RequiredArgsConstructor
//public class FlywayConfig {
//
//    private final FlywayService flywayService;
////    private final TenantService tenantService;
//
//    public void migrateFlyway(@Observes StartupEvent startupEvent) {
//
//        flywayService.initMetadataSchema();
//
////        Arrays.asList("tenant_01", "tenant_02", "tenant_03")
////                .forEach(flywayService::initNewTenantSchema);
//
////        for (Tenant tenant : tenantService.findAll()) {
////            flywayService.initNewTenantSchema(tenant.getTenantId());
////        }
//
//    }
//
//}
