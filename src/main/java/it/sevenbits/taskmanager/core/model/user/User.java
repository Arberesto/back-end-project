package it.sevenbits.taskmanager.core.model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Object to store information about taskmanager user (to login in app)
 */

public class User {

    @JsonProperty("username")
    private final String username;

    @JsonProperty("authorities")
    private final List<String> authorities;

    @JsonIgnore
    private final String password;

    /**
     * Default constructor
     * @param username username of user
     * @param password password of user
     * @param authorities authorities of user
     */

    public User(final String username, final String password, final List<String> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Constructor with no password
     * @param username username of user
     * @param authorities authorities of user
     */

    @JsonCreator
    public User(final String username, final List<String> authorities) {
        this.username = username;
        this.password = null;
        this.authorities = authorities;
    }

    /**
     * Constructor from Authentication
     * @param authentication Authentication object from Spring Security that contain all information about user
     */

    public User(final Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        password = null;

        authorities = new ArrayList<>();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            authorities.add(authority.getAuthority());
        }
    }

    public String getUsername() {
        return username;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public String getPassword() {
        return password;
    }
}