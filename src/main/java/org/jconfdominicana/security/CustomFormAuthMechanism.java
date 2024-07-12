package org.jconfdominicana.security;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.quarkus.security.AuthenticationCompletionException;
import io.quarkus.security.credential.PasswordCredential;
import io.quarkus.security.identity.IdentityProviderManager;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.AuthenticationRequest;
import io.quarkus.security.identity.request.TokenAuthenticationRequest;
import io.quarkus.security.identity.request.TrustedAuthenticationRequest;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import io.quarkus.vertx.http.runtime.security.*;
import io.smallrye.mutiny.Uni;
import io.vertx.core.MultiMap;
import io.vertx.core.http.Cookie;
import io.vertx.core.http.CookieSameSite;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import lombok.extern.slf4j.Slf4j;
import org.jconfdominicana.config.CurrentTenantResolver;

import java.net.URI;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;


@ApplicationScoped
@Priority(2)
@Slf4j
public class CustomFormAuthMechanism implements HttpAuthenticationMechanism {

    private static final String LOCATION_PAGE = "/";
    private static final String LOCATION_COOKIE = "quarkus-location-cookie";
    private static final String COOKIE_NAME = "vaadin-auth-credential";
    private static final String SCHEME = "form-custom";
    private static final String ERROR_PAGE = "login?error";
    private static final String POST_ACTION_PAGE = "/j_security_check";

    private final CookieSameSite cookieSameSite;
    private final PersistentLoginManager loginManager;

    private final CurrentTenantResolver currentTenantResolver;

    static volatile String encryptedKey;

    public CustomFormAuthMechanism(@Any CurrentTenantResolver currentTenantResolver) {
        String key;
        this.currentTenantResolver = currentTenantResolver;
        this.cookieSameSite = CookieSameSite.STRICT;
        if (encryptedKey != null) {
            key = encryptedKey;
        } else {
            byte[] data = new byte[32];
            new SecureRandom().nextBytes(data);
            key = encryptedKey = Base64.getEncoder().encodeToString(data);
        }
        this.loginManager = new PersistentLoginManager(key, COOKIE_NAME, Duration.ofMinutes(30).toMillis(), Duration.ofMinutes(1).toMillis(), false, CookieSameSite.STRICT.name(), "/");
    }


    @Override
    public Uni<SecurityIdentity> authenticate(RoutingContext context, IdentityProviderManager identityProviderManager) {
        log.info("Acessing from form auth");
        if (context.normalizedPath().endsWith(POST_ACTION_PAGE) && context.request().method().equals(HttpMethod.POST)) {
            //we always re-auth if it is a post to the auth URL
            context.put(HttpAuthenticationMechanism.class.getName(), this);
            return this.formAuth(context, identityProviderManager);
        } else {
            PersistentLoginManager.RestoreResult result = loginManager.restore(context);
            if (result != null) {
                context.put(HttpAuthenticationMechanism.class.getName(), this);
                String principal = result.getPrincipal();
                currentTenantResolver.setPrincipal(principal);
                Uni<SecurityIdentity> ret = identityProviderManager.authenticate(HttpSecurityUtils.setRoutingContextAttribute(new TrustedAuthenticationRequest(principal), context));
//                                .setRoutingContextAttribute(new CustomFormAuthenticationRequest(principal), context));
                return ret.onItem().invoke(securityIdentity -> {
                    this.loginManager.save(securityIdentity, context, result, false);
                });
            }
            return Uni.createFrom().optional(Optional.empty());
        }
    }

    public Uni<SecurityIdentity> formAuth(final RoutingContext exchange, final IdentityProviderManager securityContext) {
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
                    securityContext.authenticate(HttpSecurityUtils.setRoutingContextAttribute(new UsernamePasswordAuthenticationRequest(jUsername, new PasswordCredential(jPassword.toCharArray())), exchange)).subscribe().with(identity -> {
                        System.out.println("HttpSecurityUtils");
                        try {
                            this.loginManager.save(identity, exchange, null, false);
                            if (LOCATION_PAGE != null || exchange.request().getCookie(LOCATION_COOKIE) != null) {
                                handleRedirectBack(exchange);
                            } else {
                                exchange.response().setStatusCode(200);
                                exchange.response().end();
                            }
                            uniEmitter.complete(null);
                        } catch (Throwable t) {
                            uniEmitter.fail(t);
                        }
                    }, t -> {
                        handleRedirectBackWithError(exchange);
                        uniEmitter.fail(t);
                    });
                } catch (Throwable t) {
                    uniEmitter.fail(t);
                }
            });
            exchange.request().resume();
        });
    }

    protected void handleRedirectBackWithError(final RoutingContext exchange) {
        Cookie redirect = exchange.request().getCookie(LOCATION_COOKIE);
        String location;
        if (redirect != null) {
            verifyRedirectBackLocation(exchange.request().absoluteURI(), redirect.getValue());
            redirect.setSecure(exchange.request().isSSL());
            redirect.setSameSite(cookieSameSite);
            location = redirect.getValue();
            exchange.response().addCookie(redirect.setMaxAge(0));
        } else {
            location = exchange.request().scheme() + "://" + exchange.request().authority() + LOCATION_PAGE;
        }
        exchange.response().setStatusCode(302);
        exchange.response().headers().add(HttpHeaderNames.LOCATION, location + "login?error");
        exchange.response().end();
    }

    protected void handleRedirectBack(final RoutingContext exchange) {
        Cookie redirect = exchange.request().getCookie(LOCATION_COOKIE);
        String location;
        if (redirect != null) {
            verifyRedirectBackLocation(exchange.request().absoluteURI(), redirect.getValue());
            redirect.setSecure(exchange.request().isSSL());
            redirect.setSameSite(cookieSameSite);
            location = redirect.getValue();
            exchange.response().addCookie(redirect.setMaxAge(0));
        } else {
            location = exchange.request().scheme() + "://" + exchange.request().authority() + LOCATION_PAGE;
        }
        exchange.response().setStatusCode(302);
        exchange.response().headers().add(HttpHeaderNames.LOCATION, location);
        exchange.response().end();
    }

    protected void verifyRedirectBackLocation(String requestURIString, String redirectUriString) {
        URI requestUri = URI.create(requestURIString);
        URI redirectUri = URI.create(redirectUriString);
        if (!requestUri.getAuthority().equals(redirectUri.getAuthority()) || !requestUri.getScheme().equals(redirectUri.getScheme())) {
            log.error("Location cookie value {} does not match the current request URI {}'s scheme, host or port", redirectUriString, requestURIString);
            throw new AuthenticationCompletionException();
        }
    }

    static Uni<ChallengeData> getRedirect(final RoutingContext context, final String location) {
        String loc = context.request().scheme() + "://" + context.request().authority() + location;
        return Uni.createFrom().item(new ChallengeData(302, "Location", loc));
    }

    @Override
    public Uni<ChallengeData> getChallenge(RoutingContext context) {
        if (context.normalizedPath().endsWith(POST_ACTION_PAGE) && context.request().method().equals(HttpMethod.POST)) {
            return getRedirect(context, ERROR_PAGE);
        } else {
            return getRedirect(context, LOCATION_PAGE);
        }
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
