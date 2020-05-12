package it.sevenbits.taskmanager.core.service.login.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * Happens when trying to create user with already used username
 */

public class UserAlreadyExistsException extends AuthenticationException {

    /**
     * Default constructor
     * @param message message of exception
     */

    public UserAlreadyExistsException(final String message) {
        super(message);
    }

}
