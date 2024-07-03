package org.jconfdominicana.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinResponse;
import io.quarkus.security.runtime.SecurityIdentityAssociation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.NewCookie;
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
        Cookie cookie = new NewCookie.Builder("quarkus-credential")
                .path("/")
                .maxAge(0)
                .build();

        VaadinResponse.getCurrent().addCookie(new jakarta.servlet.http.Cookie("quarkus-credential", cookie.getValue()));
    }

}
