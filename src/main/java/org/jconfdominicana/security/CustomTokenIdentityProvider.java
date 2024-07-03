package org.jconfdominicana.security;

import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.TokenAuthenticationRequest;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.quarkus.vertx.http.runtime.security.HttpSecurityUtils;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import org.jconfdominicana.model.common.User;
import org.jconfdominicana.repositories.common.UserRepository;

import java.util.Set;

@ApplicationScoped
@AllArgsConstructor
public class CustomTokenIdentityProvider extends AbstractIdentityProvider {

    private final UserRepository userRepository;

    @Override
    protected SecurityIdentity authenticate(TokenAuthenticationRequest request) {
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

    @Override
    protected boolean requireActiveCDIRequestContext() {
        return true;
    }
}
