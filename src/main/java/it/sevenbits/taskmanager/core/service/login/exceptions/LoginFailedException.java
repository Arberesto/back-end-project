package it.sevenbits.taskmanager.core.service.login.exceptions;

import org.springframework.security.core.AuthenticationException;

public class LoginFailedException extends AuthenticationException {

    /**
     * Happens when signing failed
     * @param message message of exception
     */

    public LoginFailedException(final String message) {
        super(message);
    }

}

