package org.jconfdominicana.security;

import io.quarkus.security.credential.TokenCredential;

public class SessionTokenCredential extends TokenCredential {

    public SessionTokenCredential(String token) {
        super(token, "bearer");
    }
}
