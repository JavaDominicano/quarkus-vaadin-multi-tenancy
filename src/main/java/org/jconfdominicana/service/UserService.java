package org.jconfdominicana.service;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.jconfdominicana.repositories.common.UserRepository;

/**
 * @author me@fredpena.dev
 * @created 08/07/2024  - 10:24
 */
@ApplicationScoped
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;


    public boolean isThisUserNotAlreadyRegistered(String username) {
        return repository.findByUsername(username) == null;
    }
}
