package org.jconfdominicana.config;

//import jakarta.enterprise.context.ApplicationScoped;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
//
//@Slf4j
//@ApplicationScoped
//@RequiredArgsConstructor
//public class CurrentTenantResolver implements CurrentTenantIdentifierResolver<String> {
//
//    public static final String DEFAULT = "public";
//
//    @Override
//    public String resolveCurrentTenantIdentifier() {
//        System.out.println("resolveCurrentTenantIdentifier");
//        String tenant = TenantContext.getCurrentTenant();
//        if (tenant != null && !tenant.isEmpty()) {
//            log.info("Schema: {}", tenant);
//            return tenant;
//        }
//        log.info("Schema: {}", DEFAULT);
//        return DEFAULT;
//    }
//
//    @Override
//    public boolean validateExistingCurrentSessions() {
//        return true;
//    }
//
//
//}
