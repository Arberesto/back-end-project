package it.sevenbits.taskmanager.web.security;

import org.springframework.security.core.AuthenticationException;

/**
 * Generic exception related to Jwt.
 */
class JwtAuthenticationException extends AuthenticationException {

    JwtAuthenticationException(String message) {
        super(message);
    }

    JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

}
