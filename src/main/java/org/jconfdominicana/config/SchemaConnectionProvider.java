package org.jconfdominicana.config;

import io.agroal.api.AgroalDataSource;
import io.quarkus.hibernate.orm.runtime.customized.QuarkusConnectionProvider;

import java.sql.Connection;
import java.sql.SQLException;


public class SchemaConnectionProvider extends QuarkusConnectionProvider {

    private final String tenantId;

    public SchemaConnectionProvider(String tenantId, AgroalDataSource dataSource) {
        super(dataSource);
        this.tenantId = tenantId;
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = super.getConnection();
        connection.setSchema(this.tenantId);
        return connection;
    }
}
