package org.jconfdominicana.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinResponse;
import io.quarkus.security.runtime.SecurityIdentityAssociation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.servlet.http.Cookie;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.ext.RuntimeDelegate;
import lombok.AllArgsConstructor;

@RequestScoped
@AllArgsConstructor
public class SecurityService {

    private final SecurityIdentityAssociation sia;

    public String getAuthenticationUser() {
        return sia.getIdentity().getPrincipal().getName();
    }

    public void logout() {
        UI.getCurrent().getPage().setLocation("/login");
        NewCookie cookie = new NewCookie.Builder("quarkus-credential")
                .path("/")
                .maxAge(0)
                .build();

        VaadinResponse.getCurrent().addCookie(new Cookie("quarkus-credential", cookie.getValue()));
    }

}
