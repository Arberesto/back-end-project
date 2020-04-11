package it.sevenbits.taskmanager.web.security;

import it.sevenbits.taskmanager.core.model.user.User;
import org.springframework.security.core.Authentication;

/**
 * interface of service working with Jwt tokens
 */

public interface JwtTokenService {
    /**
     * Getter for token duration
     * @return token duration
     */
    int getTokenExpiredIn();
    /**
     * Parses the token
     * @param token the token string to parse
     * @return authenticated data
     */
    Authentication parseToken(String token);

    /**
     * Creates new Token for user.
     * @param user contains User to be represented as token
     * @return signed token
     */
    String createToken(User user);
}
