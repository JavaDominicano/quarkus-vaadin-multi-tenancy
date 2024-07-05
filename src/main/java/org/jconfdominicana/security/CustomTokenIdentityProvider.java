package org.jconfdominicana.security;

import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.TokenAuthenticationRequest;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.quarkus.vertx.http.runtime.security.HttpSecurityUtils;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jconfdominicana.model.common.User;
import org.jconfdominicana.repositories.common.UserRepository;

import java.util.Set;

@ApplicationScoped
@AllArgsConstructor
@Slf4j
public class CustomTokenIdentityProvider extends AbstractIdentityProvider {

    private final UserRepository userRepository;

    @Override
    protected SecurityIdentity authenticate(TokenAuthenticationRequest request) {
        User user = userRepository.findByUsername("admin");
        log.info("holi");
        QuarkusPrincipal principal = new QuarkusPrincipal("admin");
        QuarkusSecurityIdentity.Builder securityIdentityBuilder = QuarkusSecurityIdentity.builder()
                .setPrincipal(principal)
                .addCredential(request.getToken())
                .addRoles(Set.of("api", "ADMIN"));

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
