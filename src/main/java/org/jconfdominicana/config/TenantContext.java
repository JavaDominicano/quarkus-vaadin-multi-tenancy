package org.jconfdominicana.config;

import com.vaadin.flow.server.VaadinSession;
import org.jconfdominicana.model.common.Tenant;

/**
 * @author me@fredpena.dev
 * @created 02/07/2024  - 23:16
 */
public final class TenantContext {

    private TenantContext() {
    }

    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();


    public static String getCurrentTenant() {
        if (VaadinSession.getCurrent() != null) {
            Tenant tenant = VaadinSession.getCurrent().getAttribute(Tenant.class);
            if (tenant != null && tenant.getTenantId() != null && !tenant.getTenantId().isEmpty()) {
                return tenant.getTenantId();
            }
        }

        return currentTenant.get();
    }

    public static void setCurrentTenant(String tenant) {
        currentTenant.set(tenant);
    }

    public static void clear() {
        currentTenant.remove();
    }
}