package org.jconfdominicana.security;

import io.quarkus.arc.Arc;
import io.quarkus.arc.ManagedContext;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.TokenAuthenticationRequest;
import io.quarkus.security.jpa.runtime.JpaIdentityProvider;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.quarkus.vertx.http.runtime.security.HttpSecurityUtils;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;

import lombok.AllArgsConstructor;
import org.jconfdominicana.model.User;
import org.jconfdominicana.repositories.users.UserRepository;

import java.util.Set;

@ApplicationScoped
@AllArgsConstructor
public class IdentifyProviderToken implements IdentityProvider<TokenAuthenticationRequest> {

    private final UserRepository userRepository;

    @Override
    public Class<TokenAuthenticationRequest> getRequestType() {
        return TokenAuthenticationRequest.class;
    }

    @Override
    public Uni<SecurityIdentity> authenticate(TokenAuthenticationRequest request, AuthenticationRequestContext securityContext) {
        return securityContext.runBlocking(() -> {
            if (this.requireActiveCDIRequestContext() && !Arc.container().requestContext().isActive()) {
                ManagedContext requestContext = Arc.container().requestContext();
                requestContext.activate();

                SecurityIdentity securityIdentity;

                try {
                    securityIdentity = this.authenticate(request);
                } finally {
                    requestContext.terminate();
                }
                return securityIdentity;
            } else {
                return this.authenticate(request);
            }
        });

    }

    protected boolean requireActiveCDIRequestContext() {
        return true;
    }

    private SecurityIdentity authenticate(TokenAuthenticationRequest request) {
        User user = userRepository.findByUsername("admin");

        QuarkusPrincipal principal = new QuarkusPrincipal(user.getUsername());
        QuarkusSecurityIdentity.Builder securityIdentityBuilder = QuarkusSecurityIdentity.builder()
                .setPrincipal(principal)
                .addCredential(request.getToken())
                .addRoles(Set.of("api", user.getRole()));

        RoutingContext routingContext = HttpSecurityUtils.getRoutingContextAttribute(request);
        if (routingContext != null) {
            securityIdentityBuilder.addAttribute(RoutingContext.class.getName(), routingContext);
        }

        return securityIdentityBuilder.build();
    }
}
