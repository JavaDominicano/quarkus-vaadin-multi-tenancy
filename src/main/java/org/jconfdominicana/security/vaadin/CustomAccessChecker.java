package org.jconfdominicana.security.vaadin;

import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import org.jconfdominicana.model.User;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author me@fredpena.dev
 * @created 02/07/2024  - 21:56
 */
@ApplicationScoped
@AllArgsConstructor
public class CustomAccessChecker extends AccessAnnotationChecker {

    private final SecurityService securityService;

    @Override
    public boolean hasAccess(Class<?> securedClass) {
        if (securedClass.isAnnotationPresent(AnonymousAllowed.class) || securedClass.isAnnotationPresent(PermitAll.class)) {
            return true;
        }

        if (securedClass.isAnnotationPresent(RolesAllowed.class)) {
            RolesAllowed rolesAllowed = securedClass.getAnnotation(RolesAllowed.class);
            Optional<User> authenticationUser = securityService.getAuthenticationUser();
            if (authenticationUser.isPresent()) {
                String role = authenticationUser.get().getRole();
                return Arrays.asList(rolesAllowed.value()).contains(role);
            }
        }

        return false;
    }
}
