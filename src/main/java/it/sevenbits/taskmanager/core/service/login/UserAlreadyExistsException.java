package it.sevenbits.taskmanager.core.service.login;

import org.springframework.security.core.AuthenticationException;

public class UserAlreadyExistsException extends AuthenticationException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }

}
