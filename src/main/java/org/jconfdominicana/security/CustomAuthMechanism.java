package org.jconfdominicana.security;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.security.identity.IdentityProviderManager;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.AuthenticationRequest;
import io.quarkus.security.identity.request.TokenAuthenticationRequest;
import io.quarkus.vertx.http.runtime.security.ChallengeData;
import io.quarkus.vertx.http.runtime.security.HttpAuthenticationMechanism;
import io.quarkus.vertx.http.runtime.security.HttpCredentialTransport;
import io.quarkus.vertx.http.runtime.security.HttpSecurityUtils;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Collections;
import java.util.Set;
import java.util.logging.Logger;

@ApplicationScoped
public class CustomAuthMechanism implements HttpAuthenticationMechanism {

    private static final String SCHEME = "Bearer";

    public static final Logger LOGGER = Logger.getLogger(CustomAuthMechanism.class.getName());


    @Override
    public Uni<SecurityIdentity> authenticate(RoutingContext context, final IdentityProviderManager identityProviderManager) {
        LOGGER.info("Accessing from api route");

        context.put(HttpAuthenticationMechanism.class.getName(), this);
        AuthenticationRequest credentials = new TokenAuthenticationRequest(new SessionTokenCredential(""));
        return identityProviderManager.authenticate(HttpSecurityUtils
                .setRoutingContextAttribute(credentials, context));
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
}
