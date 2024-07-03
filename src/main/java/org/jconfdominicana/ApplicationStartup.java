package org.jconfdominicana;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jconfdominicana.model.User;
import org.jconfdominicana.repositories.users.UserRepository;
import org.jconfdominicana.security.vaadin.Role;

import java.util.List;

@ApplicationScoped
public class ApplicationStartup {

    @Inject
    UserRepository userRepository;

    @Transactional
    public void start(@Observes StartupEvent startupEvent) {

        User admin = User.builder()
                .username("admin")
                .password(BcryptUtil.bcryptHash("admin"))
                .role(Role.ADMIN.name())
                .build();

        User user = User.builder()
                .username("user")
                .password(BcryptUtil.bcryptHash("user"))
                .role(Role.USER.name())
                .build();

        userRepository.insertAll(List.of(admin, user));
    }
}
