package it.sevenbits.taskmanager.web.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.sevenbits.taskmanager.web.security.Token;

/**
Model to return token after successful sign-in operation in UsersController
 */

public class SignInResponse {
    @JsonProperty("token")
    private Token token;

    /**
     * Default constructor
     * @param token Token object to store
     */

    public SignInResponse(final Token token) {
        this.token = token;
    }

    public String getToken() {
        return token.getToken();
    }
}
