package it.sevenbits.taskmanager.web.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Jwt token that send outside application
 */

public class Token {

    private final String token;

    /**
     * Default constructor
     * @param token String that contains token
     */

    @JsonCreator
    public Token(@JsonProperty("token") final String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
