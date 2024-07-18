package org.jconfdominicana.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import org.jconfdominicana.model.common.User;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

@ApplicationScoped
public class JwtService {

    public static final Integer SECONDS = 1;
    public static final Integer MINUTE = 60 * SECONDS;
    public static final Integer HOUR = 60 * MINUTE;

    private static final Logger LOGGER = Logger.getLogger(JwtService.class.getName());

    private final Algorithm algorithm;

    public JwtService() {
        this.algorithm = Algorithm.HMAC256("5fd591e87e52ee677115be6294f85a2ea13d248b6344b6ba27f12ce8faa".getBytes(StandardCharsets.UTF_8));
    }

    public DecodedJWT validateToken(String token) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Validating token: %s", token));
        }

        try {
            JWTVerifier jwtVerifier = JWT.require(this.algorithm)
                    .withIssuer("multi-tenant")
                    .build();

            return jwtVerifier.verify(token);

        } catch (JWTVerificationException e) {
            LOGGER.error("JWT verification failed", e);
            return null;
        }
    }

    public String generateToken(User user) {
        Instant now = Instant.now();
        return JWT.create()
                .withIssuer("multi-tenant")
                .withSubject(user.getUsername())
                .withClaim("scopes", "admin")
                .withClaim("x-tenant-id", "admin01")
                .withIssuedAt(now)
                .withExpiresAt(now.plusSeconds(HOUR))
                .sign(this.algorithm);
    }
}
