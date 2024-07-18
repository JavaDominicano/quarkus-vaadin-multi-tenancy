package org.jconfdominicana.security;

import io.quarkus.security.credential.TokenCredential;

public class JwtTokenCredential extends TokenCredential {

    public JwtTokenCredential(String token) {
        super(token, "bearer");
    }
}
