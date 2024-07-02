package org.jconfdominicana;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jconfdominicana.model.User;
import org.jconfdominicana.repositories.users.UserRepository;

@ApplicationScoped
public class ApplicationStartup {

    @Inject
    UserRepository userRepository;

    @Transactional
    public void start(@Observes StartupEvent startupEvent) {
        String passwordHash = BcryptUtil.bcryptHash("admin");

        User user = User.builder()
                .username("admin")
                .password(passwordHash)
                .role("admin")
                .build();
        userRepository.insert(user);
    }
}
