package org.jconfdominicana.security;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import org.jconfdominicana.config.TenantContext;
import org.jconfdominicana.services.TenantService;

import java.io.IOException;

@Provider
public class TenantInterceptor implements ContainerRequestFilter, ContainerResponseFilter {

    public static final String EXCLUDE_PATH = "/api/auth/login";

    @Inject
    TenantService tenantService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        if (EXCLUDE_PATH.equals(path)) return;

        String tenantId = requestContext.getHeaderString("X-Tenant-ID");
//        Tenant tenant = tenantService.findByTenantName(tenantId);

        if (tenantId != null && !tenantId.isEmpty()) {
            TenantContext.setCurrentTenant(tenantId);
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        TenantContext.clear();
    }
}
