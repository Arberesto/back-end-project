package it.sevenbits.taskmanager.web.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.sevenbits.taskmanager.web.security.Token;

/**
Model to return token after successful sign-in operation
 */

public class SignInResponse {
    @JsonProperty("token")
    private Token token;

    public SignInResponse(final Token token) {
        this.token = token;
    }

    public String getToken() {
        return token.getToken();
    }
}
