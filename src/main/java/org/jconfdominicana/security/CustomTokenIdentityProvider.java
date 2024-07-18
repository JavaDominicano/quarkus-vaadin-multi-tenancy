package org.jconfdominicana.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import io.quarkus.hibernate.orm.runtime.tenant.TenantResolver;
import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.security.credential.TokenCredential;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.TokenAuthenticationRequest;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.quarkus.vertx.http.runtime.security.HttpSecurityUtils;
import io.vertx.ext.web.RoutingContext;
import jakarta.data.exceptions.EmptyResultException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.jconfdominicana.config.CurrentTenantResolver;
import org.jconfdominicana.config.TenantContext;
import org.jconfdominicana.model.common.User;
import org.jconfdominicana.repositories.common.UserRepository;
import org.jconfdominicana.services.JwtService;

@ApplicationScoped
@Slf4j
public class CustomTokenIdentityProvider extends AbstractIdentityProvider {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Inject
    public CustomTokenIdentityProvider(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected SecurityIdentity authenticate(TokenAuthenticationRequest request) {
        TokenCredential tokenCredential = request.getToken();
        if (tokenCredential instanceof JwtTokenCredential) {
            String token = tokenCredential.getToken();
            DecodedJWT decodedJWT = jwtService.validateToken(token);
            if (decodedJWT == null) throw new AuthenticationFailedException();

            String subject = decodedJWT.getSubject();

            User user = this.getUser(subject);
            QuarkusPrincipal principal = new QuarkusPrincipal(user.getUsername());
            QuarkusSecurityIdentity.Builder securityIdentityBuilder = QuarkusSecurityIdentity.builder()
                    .setPrincipal(principal)
                    .addCredential(tokenCredential)
                    .addRole(user.getRole());

            RoutingContext routingContext = HttpSecurityUtils.getRoutingContextAttribute(request);
            if (routingContext != null) {
//                TenantContext.setCurrentTenant(routingContext.request().getHeader(TenantContext.PRIVATE_TENANT_HEADER));
                securityIdentityBuilder.addAttribute(RoutingContext.class.getName(), routingContext);
            }


            return securityIdentityBuilder.build();
        }

        throw new AuthenticationFailedException();

    }

    private User getUser(String username) {
        try {
            return userRepository.findByUsername(username);
        } catch (EmptyResultException emptyResultException) {
            throw new AuthenticationFailedException();
        }
    }

    @Override
    protected boolean requireActiveCDIRequestContext() {
        return true;
    }
}
