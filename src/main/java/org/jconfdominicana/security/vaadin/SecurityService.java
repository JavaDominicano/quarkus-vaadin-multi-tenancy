package org.jconfdominicana.security.vaadin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinResponse;
import io.quarkus.security.runtime.SecurityIdentityAssociation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.servlet.http.Cookie;
import jakarta.ws.rs.core.NewCookie;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jconfdominicana.model.Profile;
import org.jconfdominicana.model.common.Tenant;
import org.jconfdominicana.model.common.User;
import org.jconfdominicana.repositories.ProfileRepository;
import org.jconfdominicana.repositories.common.UserRepository;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@RequestScoped
@AllArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final CacheService cacheService;
    private final SecurityIdentityAssociation sia;


    public Optional<String> getUsername() {
        String username = sia.getIdentity().getPrincipal().getName();

        System.out.println("SecurityService: "+username);

        if (username == null || username.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(username);
    }

    public Optional<User> getUser() {
        String username = sia.getIdentity().getPrincipal().getName();

        if (username == null || username.isEmpty()) {
            return Optional.empty();
        }

        User user = userRepository.findByUsername(username);

        log.info("User: username = = {}", user.getUsername());

        cacheService.putUser(user.getUsername(), user);

        return Optional.of(user);
    }

    public Optional<Profile> getProfile() {
        try {
            String username = sia.getIdentity().getPrincipal().getName();

            if (username == null || username.isEmpty()) {
                return Optional.empty();
            }

            log.info("User: username = = {}", username);

            Tenant tenant = cacheService.getTenant(username);
            if (tenant != null) {
                return Optional.empty();
            }

            Profile currentProfile = cacheService.getProfile(username);
            if (currentProfile != null) {
                return Optional.of(currentProfile);
            }

            Profile profile = profileRepository.findByUsername(username);
            if (profile == null) {
                return Optional.empty();
            }
            log.info("Profile: username = = {}", profile.getUsername());

            cacheService.putProfile(profile.getUsername(), profile);

            return Optional.of(profile);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    public Optional<Boolean> userHasSomeTenant() {
        try {
            String username = sia.getIdentity().getPrincipal().getName();

            if (username == null || username.isEmpty()) {
                return Optional.empty();
            }

            User user = cacheService.getUser(username);
            return Optional.of(user.getTenants().size() > 1);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearSession() {
//        VaadinSession.getCurrent().setAttribute(Tenant.class, null);
//        VaadinSession.getCurrent().setAttribute(User.class, null);
    }


    public void logout() {
        clearSession();

        UI.getCurrent().getPage().setLocation("/login");
        NewCookie cookie = new NewCookie.Builder("quarkus-credential").path("/").maxAge(0).build();

        VaadinResponse.getCurrent().addCookie(new Cookie("quarkus-credential", cookie.getValue()));
    }


}
