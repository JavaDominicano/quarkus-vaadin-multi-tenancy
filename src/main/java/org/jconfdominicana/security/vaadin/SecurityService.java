package org.jconfdominicana.security.vaadin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.server.VaadinSession;
import io.quarkus.security.runtime.SecurityIdentityAssociation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.servlet.http.Cookie;
import jakarta.ws.rs.core.NewCookie;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jconfdominicana.config.TenantContext;
import org.jconfdominicana.model.User;
import org.jconfdominicana.repositories.users.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequestScoped
@AllArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;
    private final SecurityIdentityAssociation sia;

    public Optional<User> getAuthenticationUser() {
        String username = sia.getIdentity().getPrincipal().getName();

        if (username == null || username.isEmpty()) {
            return Optional.empty();
        }

        User user = userRepository.findByUsername(username);
        log.info("Authenticate: userid = = {}", user.getId());

        VaadinSession.getCurrent().setAttribute(User.class, user);

        String uuid = UUID.randomUUID().toString();
        log.info("UUID = {}", uuid);

        TenantContext.setCurrentTenant(uuid);

        return Optional.of(user);
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
