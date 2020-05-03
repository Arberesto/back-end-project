package it.sevenbits.taskmanager.core.service.login.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidBodyException extends AuthenticationException {

        /**
         * Happens when request body doesn't contain all certain fields
         * @param message message of exception
         */

        public InvalidBodyException(final String message) {
            super(message);
        }

    }
