package org.jconfdominicana.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.jconfdominicana.model.common.User;

import java.util.Objects;

@ApplicationScoped
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    public User login(String username, String password) {
        Objects.requireNonNull(password, "Password cannot be null");

        User user = this.userService.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));


        boolean matched = this.userService.matchesPassword(user.getPassword(), password);

        if (matched) {
            return user;
        }

        throw new InternalServerErrorException();
    }


}
