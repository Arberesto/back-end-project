package it.sevenbits.taskmanager.core.service.login.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserAlreadyExistsException extends AuthenticationException {

    /**
     * Happens when trying to create user with already used username
     * @param message message of exception
     */

    public UserAlreadyExistsException(final String message) {
        super(message);
    }

}
