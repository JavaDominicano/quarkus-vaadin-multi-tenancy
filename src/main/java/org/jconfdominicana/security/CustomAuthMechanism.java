package org.jconfdominicana.security;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.security.identity.IdentityProviderManager;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.AuthenticationRequest;
import io.quarkus.security.identity.request.TokenAuthenticationRequest;
import io.quarkus.vertx.http.runtime.security.*;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.jconfdominicana.config.TenantContext;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

@ApplicationScoped
@Priority(1)
@RequiredArgsConstructor
public class CustomAuthMechanism implements HttpAuthenticationMechanism {

    private static final String SCHEME = "Bearer";

    public static final Logger LOGGER = Logger.getLogger(CustomAuthMechanism.class.getName());

    private final TenantContext tenantContext;


    @Override
    public Uni<SecurityIdentity> authenticate(RoutingContext context, final IdentityProviderManager identityProviderManager) {
        LOGGER.info("Accessing from api route");
        String token = new AuthTokenExtractor(context)
                .getRequestToken();

        if (token != null) {
            context.put(HttpAuthenticationMechanism.class.getName(), this);
            AuthenticationRequest credentials = new TokenAuthenticationRequest(new JwtTokenCredential(token));
            return identityProviderManager.authenticate(HttpSecurityUtils
                    .setRoutingContextAttribute(credentials, context));
        }

        return Uni.createFrom().optional(Optional.empty());


    }

    @Override
    public Uni<ChallengeData> getChallenge(RoutingContext context) {
        ChallengeData res = new ChallengeData(
                HttpResponseStatus.UNAUTHORIZED.code(),
                HttpHeaderNames.WWW_AUTHENTICATE,
                SCHEME
        );
        return Uni.createFrom().item(res);
    }

    @Override
    public Uni<HttpCredentialTransport> getCredentialTransport(RoutingContext context) {
        return Uni.createFrom().item(new HttpCredentialTransport(HttpCredentialTransport.Type.AUTHORIZATION, SCHEME));
    }

    @Override
    public Set<Class<? extends AuthenticationRequest>> getCredentialTypes() {
        return Collections.singleton(TokenAuthenticationRequest.class);
    }

    private static class AuthTokenExtractor extends TokenExtractor {
        private final RoutingContext routingContext;

        public AuthTokenExtractor(RoutingContext routingContext) {
            this.routingContext = routingContext;
        }

        @Override
        public String headerValue() {
            return routingContext.request()
                    .getHeader(HttpHeaders.AUTHORIZATION);
        }
    }
}
