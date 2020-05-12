package it.sevenbits.taskmanager.core.service.login.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * Happens when request body doesn't contain all certain fields
 */

public class InvalidBodyException extends AuthenticationException {

        /**
         * Default constructor
         * @param message message of exception
         */

        public InvalidBodyException(final String message) {
            super(message);
        }

    }
