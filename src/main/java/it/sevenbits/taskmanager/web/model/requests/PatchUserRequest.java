package it.sevenbits.taskmanager.web.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.sevenbits.taskmanager.web.security.AuthoritiesList;

import java.util.List;

/**
 * Model for PATCH method in UsersController
 */

public class PatchUserRequest {

    //ADMIN can change only account status and user authorities.
    /**
     * Model for PATCH Request in UsersController
     */

    @JsonProperty
    private Boolean enabled;
    @JsonProperty
    private List<String> authorities;

    /**
     * Constructor for deserialization
     */

    public PatchUserRequest(){}

    /**
     * Default constructor
     * @param enabled if user enabled
     * @param authorities authorities of user
     */

    public PatchUserRequest(final boolean enabled, final List<String> authorities) {
        this.enabled = enabled;
        this.authorities = authorities;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    /**
     * Validate fields of PatchUserRequest
     * @return true if correct, false if incorrect
     */

    public boolean validate() {
        for (String authority : authorities) {
            if (AuthoritiesList.resolveString(authority) == null) {
                return false;
            }
        }
        return true;

    }
}
