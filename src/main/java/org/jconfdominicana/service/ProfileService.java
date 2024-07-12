package org.jconfdominicana.service;

import jakarta.data.exceptions.EmptyResultException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jconfdominicana.model.Profile;
import org.jconfdominicana.repositories.ProfileRepository;

import java.util.Optional;

/**
 * @author me@fredpena.dev
 * @created 08/07/2024  - 10:24
 */
@ApplicationScoped
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository repository;

    public Optional<Profile> findByUsername(String username) {
        try {
            return Optional.ofNullable(repository.findByUsername(username));
        } catch (EmptyResultException ex) {
            return Optional.empty();
        }
    }


    @Transactional
    public Profile insert(Profile element) {
        return repository.insert(element);
    }

    @Transactional
    public Profile update(Profile element) {
        return repository.update(element);
    }
}
