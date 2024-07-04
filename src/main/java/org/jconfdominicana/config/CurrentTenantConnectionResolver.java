package org.jconfdominicana.config;

import io.agroal.api.AgroalDataSource;
import io.quarkus.hibernate.orm.PersistenceUnitExtension;
import io.quarkus.hibernate.orm.runtime.tenant.TenantConnectionResolver;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

@PersistenceUnitExtension
@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class CurrentTenantConnectionResolver implements TenantConnectionResolver {

    private final AgroalDataSource dataSource;

    @Override
    public ConnectionProvider resolve(String tenantId) {
        log.info("Resolving tenant: {}", tenantId);
        return new SchemaConnectionProvider(tenantId, dataSource);
    }
}
