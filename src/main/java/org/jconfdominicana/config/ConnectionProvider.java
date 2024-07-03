package org.jconfdominicana.config;

//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.enterprise.context.RequestScoped;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
//
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.SQLException;
//
//@Slf4j
//@ApplicationScoped
//@RequiredArgsConstructor
//public class ConnectionProvider implements MultiTenantConnectionProvider<String> {
//
//    private final transient DataSource dataSource;
//
//
//    @Override
//    public Connection getAnyConnection() throws SQLException {
//        return getConnection(CurrentTenantResolver.DEFAULT);
//    }
//
//    @Override
//    public void releaseAnyConnection(Connection connection) throws SQLException {
//        connection.close();
//    }
//
//    @Override
//    public Connection getConnection(String tenantId) throws SQLException {
//        log.trace("Get connection for tenant '{}'", tenantId);
//        Connection connection = dataSource.getConnection();
//        connection.setSchema(tenantId);
//        return connection;
//    }
//
//
//    @Override
//    public void releaseConnection(String tenantId, Connection connection) throws SQLException {
//        log.trace("Release connection for tenant '{}'", tenantId);
//        connection.setSchema(CurrentTenantResolver.DEFAULT);
//        connection.close();
//    }
//
//    @Override
//    public boolean supportsAggressiveRelease() {
//        return false;
//    }
//
//    @Override
//    public boolean isUnwrappableAs(Class<?> aClass) {
//        return false;
//    }
//
//    @Override
//    public <T> T unwrap(Class<T> aClass) {
//        return null;
//    }
//}
