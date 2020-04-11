package it.sevenbits.taskmanager.web.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Component
public class JwtSettings {


    //TODO: add work with refresh token
    private String tokenIssuer;
    private String tokenSigningKey;
    private int aTokenDuration;

    public JwtSettings(@Value("${jwt.issuer}") final String tokenIssuer,
                       @Value("${jwt.signingKey}") final String tokenSigningKey,
                       @Value("${jwt.aTokenDuration}") final int aTokenDuration) {
        this.tokenIssuer = tokenIssuer;
        this.tokenSigningKey = tokenSigningKey;
        this.aTokenDuration = aTokenDuration;
    }

    String getTokenIssuer() {
        return tokenIssuer;
    }

    byte[] getTokenSigningKey() {
        return tokenSigningKey.getBytes(StandardCharsets.UTF_8);
    }

    Duration getTokenExpiredIn() {
        return Duration.ofMinutes(aTokenDuration);
    }
}
