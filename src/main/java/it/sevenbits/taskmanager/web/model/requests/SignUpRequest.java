package it.sevenbits.taskmanager.web.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model to receive username and password to create new user.
 */

public class SignUpRequest {
    @JsonProperty
    private String username;
    private String password;

    public SignUpRequest(@JsonProperty("username") final String username, @JsonProperty("password") final String password) {
        this.username = username;
        this.password = password;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
