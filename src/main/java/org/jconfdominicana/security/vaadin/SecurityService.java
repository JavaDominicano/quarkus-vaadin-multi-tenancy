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
import org.jconfdominicana.model.Profile;
import org.jconfdominicana.repositories.ProfileRepository;
import org.jconfdominicana.repositories.common.UserRepository;

import java.util.Optional;

@Slf4j
@RequestScoped
@AllArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final SecurityIdentityAssociation sia;

//    public Optional<User> getUser() {
//        String username = sia.getIdentity().getPrincipal().getName();
//
//        if (username == null || username.isEmpty()) {
//            return Optional.empty();
//        }
//
//        User user = userRepository.findByUsername(username);
//
//        log.info("User: username = = {}", user.getUsername());
//
//        VaadinSession.getCurrent().setAttribute(User.class, user);
//
//        return Optional.of(user);
//    }

    public Optional<Profile> getProfile() {
        if (TenantContext.getCurrentTenant() == null || TenantContext.getCurrentTenant().isEmpty()) {
            return Optional.empty();
        }

        Profile currentProfile = VaadinSession.getCurrent().getAttribute(Profile.class);
        if (currentProfile != null) {
            return Optional.of(currentProfile);
        }

        String username = sia.getIdentity().getPrincipal().getName();

        if (username == null || username.isEmpty()) {
            return Optional.empty();
        }
        log.info("User: username = = {}", username);

        Profile profile = profileRepository.findByUsername(username);
        if (profile == null) {
            return Optional.empty();
        }
        log.info("Profile: username = = {}", profile.getUsername());

        VaadinSession.getCurrent().setAttribute(Profile.class, profile);

        return Optional.of(profile);
    }


    public void logout() {
        VaadinSession.getCurrent().close();

        UI.getCurrent().getPage().setLocation("/login");
        NewCookie cookie = new NewCookie.Builder("quarkus-credential")
                .path("/")
                .maxAge(0)
                .build();

        VaadinResponse.getCurrent().addCookie(new Cookie("quarkus-credential", cookie.getValue()));
    }


}
