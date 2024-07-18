package org.jconfdominicana.config;

import jakarta.inject.Singleton;


@Singleton
public class TenantContext {

    public static final String PRIVATE_TENANT_HEADER = "X-Tenant-ID";
    private static final String DEFAULT_TENANT = "public";

    private static final ThreadLocal<String> currentTenant = new InheritableThreadLocal<>();

    public static String getCurrentTenant() {
        return currentTenant.get();
    }

    public static void setCurrentTenant(String tenant) {
        currentTenant.set(tenant);
    }

    public void clear() {
        currentTenant.remove();
    }
}
