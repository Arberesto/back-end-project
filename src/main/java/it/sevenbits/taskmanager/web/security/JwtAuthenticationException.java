package it.sevenbits.taskmanager.web.security;

import org.springframework.security.core.AuthenticationException;

/**
 * Generic exception related to Jwt.
 */
class JwtAuthenticationException extends AuthenticationException {

    /**
     * Default constructor
     * @param message message of exception
     */

    JwtAuthenticationException(final String message) {
        super(message);
    }

    /**
     * Constructor if something else caused this exception
     * @param message message of exception
     * @param cause Throwable object
     */

    JwtAuthenticationException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
