package org.jconfdominicana.services;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.data.exceptions.EmptyResultException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jconfdominicana.model.common.User;
import org.jconfdominicana.repositories.common.UserRepository;
import org.wildfly.security.password.Password;
import org.wildfly.security.password.PasswordFactory;
import org.wildfly.security.password.WildFlyElytronPasswordProvider;
import org.wildfly.security.password.interfaces.BCryptPassword;
import org.wildfly.security.password.util.ModularCrypt;

import java.util.Optional;

/**
 * @author me@fredpena.dev
 * @created 08/07/2024  - 10:24
 */
@ApplicationScoped
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public Optional<User> findByUsername(String username) {
        try {
            return Optional.ofNullable(repository.findByUsername(username));
        } catch (EmptyResultException ex) {
            return Optional.empty();
        }
    }

    public boolean isThisUserNotAlreadyRegistered(String username) {
        try {
            return repository.findByUsername(username) == null;
        } catch (EmptyResultException ex) {
            return true;
        }
    }

    public boolean matchesPassword(String bcryptPasswordHash, String password) {
        return BcryptUtil.matches(password, bcryptPasswordHash);
    }

    @Transactional
    public User insert(User element) {
        return repository.insert(element);
    }

    @Transactional
    public User update(User element) {
        return repository.update(element);
    }
}
