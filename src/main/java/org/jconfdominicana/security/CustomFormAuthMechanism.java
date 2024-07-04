package org.jconfdominicana.security;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.security.credential.PasswordCredential;
import io.quarkus.security.identity.IdentityProviderManager;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.AuthenticationRequest;
import io.quarkus.security.identity.request.TokenAuthenticationRequest;
import io.quarkus.security.identity.request.TrustedAuthenticationRequest;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import io.quarkus.vertx.http.runtime.security.ChallengeData;
import io.quarkus.vertx.http.runtime.security.HttpAuthenticationMechanism;
import io.quarkus.vertx.http.runtime.security.HttpCredentialTransport;
import io.quarkus.vertx.http.runtime.security.HttpSecurityUtils;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.UniEmitter;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Logger;

@ApplicationScoped
@Priority(2)
@Slf4j
public class CustomFormAuthMechanism implements HttpAuthenticationMechanism {

    private static final String SCHEME = "form-custom";


    @Override
    public Uni<SecurityIdentity> authenticate(RoutingContext context,
                                              IdentityProviderManager identityProviderManager) {
        log.info("Acessing from form auth");
        if (context.normalizedPath().endsWith("/j_security_check") && context.request().method().equals(HttpMethod.POST)) {
            System.out.println("entro aqui");
            //we always re-auth if it is a post to the auth URL
            context.put(HttpAuthenticationMechanism.class.getName(), this);
            return this.formAuth(context, identityProviderManager);
        } else {
            context.put(HttpAuthenticationMechanism.class.getName(), this);
//                Uni<SecurityIdentity> ret = identityProviderManager
//                        .authenticate(HttpSecurityUtils
//                                .setRoutingContextAttribute(new UsernamePasswordAuthenticationRequest("", new PasswordCredential("".toCharArray())), context));
//                return ret.onItem().invoke(securityIdentity -> {});
            return Uni.createFrom().optional(Optional.empty());
        }
    }

    public Uni<SecurityIdentity> formAuth(final RoutingContext exchange,
                                          final IdentityProviderManager securityContext) {
        exchange.request().setExpectMultipart(true);
        return Uni.createFrom().emitter(uniEmitter -> {
            exchange.request().endHandler(event -> {
                try {
                    MultiMap res = exchange.request().formAttributes();

                    final String jUsername = res.get("username");
                    final String jPassword = res.get("password");
                    if (jUsername == null || jPassword == null) {
                        uniEmitter.complete(null);
                        return;
                    }
                    securityContext
                            .authenticate(HttpSecurityUtils
                                    .setRoutingContextAttribute(new UsernamePasswordAuthenticationRequest(jUsername,
                                            new PasswordCredential(jPassword.toCharArray())), exchange))
                            .subscribe().with(identity -> {
                                try {
                                    exchange.response().setStatusCode(200);
                                    exchange.response().end();
                                    uniEmitter.complete(null);
                                } catch (Throwable t) {
                                    uniEmitter.fail(t);
                                }
                            }, throwable -> uniEmitter.fail(throwable));
                } catch (Throwable t) {
                    uniEmitter.fail(t);
                }
            });
            exchange.request().resume();
        });
    }

    static Uni<ChallengeData> getRedirect(final RoutingContext context, final String location) {
        String loc = context.request().scheme() + "://" + context.request().authority() + location;
        return Uni.createFrom().item(new ChallengeData(302, "Location", loc));
    }

    @Override
    public Uni<ChallengeData> getChallenge(RoutingContext context) {
        if (context.normalizedPath().endsWith("/j_security_check") && context.request().method().equals(HttpMethod.POST)) {
            return getRedirect(context, "login?error");
        } else {
            ChallengeData res = new ChallengeData(
                    HttpResponseStatus.UNAUTHORIZED.code(),
                    null,
                    null
            );
            return Uni.createFrom().item(res);
        }
//
    }

    @Override
    public Uni<HttpCredentialTransport> getCredentialTransport(RoutingContext context) {
        return Uni.createFrom().item(new HttpCredentialTransport(HttpCredentialTransport.Type.POST, SCHEME));
    }

    @Override
    public Set<Class<? extends AuthenticationRequest>> getCredentialTypes() {
        return Collections.singleton(TokenAuthenticationRequest.class);
    }
}
