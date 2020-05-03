package it.sevenbits.taskmanager.web.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model to receive username and password to sign in into existing account in UsersController
 */

public class SignInRequest {
    @JsonProperty
    private String username;
    @JsonProperty
    private String password;

    /**
     * Default constructor
     * @param username username of user
     * @param password password of user
     */

    public SignInRequest(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return String.format("{\n 'username':'%s',\n 'password':'%s'\n}", username, password);
    }
}
