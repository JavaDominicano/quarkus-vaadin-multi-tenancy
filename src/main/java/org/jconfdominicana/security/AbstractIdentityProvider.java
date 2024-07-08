package org.jconfdominicana.security;

import io.quarkus.arc.Arc;
import io.quarkus.arc.ManagedContext;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.TokenAuthenticationRequest;
import io.smallrye.mutiny.Uni;

public abstract class AbstractIdentityProvider implements IdentityProvider<TokenAuthenticationRequest> {

    @Override
    public Uni<SecurityIdentity> authenticate(TokenAuthenticationRequest request, AuthenticationRequestContext context) {
        return context.runBlocking(() -> createSecurityIdentity(request));
    }

    @Override
    public Class<TokenAuthenticationRequest> getRequestType() {
        return TokenAuthenticationRequest.class;
    }

    protected SecurityIdentity createSecurityIdentity(TokenAuthenticationRequest request) {
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
    }

        protected boolean requireActiveCDIRequestContext() {
            return false;
        }

    protected abstract SecurityIdentity authenticate(TokenAuthenticationRequest request);
}
