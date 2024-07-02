package org.jconfdominicana.security;

import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinSession;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.TokenAuthenticationRequest;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.quarkus.vertx.http.runtime.security.HttpSecurityUtils;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IdentifyProviderVaadin implements IdentityProvider<TokenAuthenticationRequest> {

    @Override
    public Class<TokenAuthenticationRequest> getRequestType() {
        return TokenAuthenticationRequest.class;
    }

    @Override
    public Uni<SecurityIdentity> authenticate(TokenAuthenticationRequest request, AuthenticationRequestContext authenticationRequestContext) {
        return authenticationRequestContext.runBlocking(() -> {
            QuarkusPrincipal principal = new QuarkusPrincipal("juandi");
            QuarkusSecurityIdentity.Builder securityIdentityBuilder = QuarkusSecurityIdentity.builder()
                    .setPrincipal(principal)
                    .addCredential(request.getToken())
                    .addRole("ADMIN");

            RoutingContext routingContext = HttpSecurityUtils.getRoutingContextAttribute(request);
            if (routingContext != null) {
                securityIdentityBuilder.addAttribute(RoutingContext.class.getName(), routingContext);
            }
            return securityIdentityBuilder.build();
        });

    }
}
