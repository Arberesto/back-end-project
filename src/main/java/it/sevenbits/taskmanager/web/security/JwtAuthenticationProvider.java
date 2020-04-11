package it.sevenbits.taskmanager.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Authentication provider which is able to verify JWT tokens.
 */
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final JwtTokenService tokenService;

    /**
     * Default constructor
     * @param tokenService service to work with Jwt tokens
     */

    public JwtAuthenticationProvider(final JwtTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        String token = String.valueOf(authentication.getCredentials());
        logger.debug("Authenticating {}", token);

        try {
            return tokenService.parseToken(token);
        } catch (Exception e) {
            throw new JwtAuthenticationException("Invalid token received", e);
        }
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return (JwtToken.class.isAssignableFrom(authentication));
    }

}
