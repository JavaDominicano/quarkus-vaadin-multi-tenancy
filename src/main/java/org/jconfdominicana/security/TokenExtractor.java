package org.jconfdominicana.security;

public abstract class TokenExtractor {

    private static final String BEARER = "Bearer";
    private static final String BEARER_PREFIX = BEARER + " ";

    public String getRequestToken() {

        String headerValue = this.headerValue();

        if (headerValue != null) {
            if (!headerValue.startsWith(BEARER_PREFIX)) return null;
            return headerValue.split(BEARER_PREFIX)[1];
        }

        return null;

    }

    public abstract String headerValue();


}
