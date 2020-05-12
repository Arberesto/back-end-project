package it.sevenbits.taskmanager.core.service.login.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * Happens when sign in operation failed
 */

public class LoginFailedException extends AuthenticationException {

    /**
     * Default constructor
     * @param message message of exception
     */

    public LoginFailedException(final String message) {
        super(message);
    }

}

