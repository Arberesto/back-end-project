package it.sevenbits.taskmanager.core.model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Object to store information about taskmanager user (to login in app)
 */

public class User {

    @JsonProperty("id")
    private final String id;

    @JsonProperty("username")
    private final String username;

    //TODO: change List to Set

    @JsonProperty("authorities")
    private final List<String> authorities;

    @JsonIgnore
    private final String password;

    private final boolean enabled;

    /**
     * Default constructor to create new user
     *
     * @param username    username of user
     * @param password    password of user
     * @param authorities authorities of user
     */

    public User(final String username, final String password, final List<String> authorities) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.enabled = true;
    }

    /**
     * Constructor to create existing user
     *
     * @param id          id of user
     * @param username    username of user
     * @param password    password of user
     * @param authorities authorities of user
     * @param enabled status of user (active or not)
     */

    public User(final String id, final String username, final String password, final List<String> authorities,
                final boolean enabled) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
    }

    /**
     * Constructor with no password
     *
     * @param username    username of user
     * @param authorities authorities of user
     */

    @JsonCreator
    public User(final String username, final List<String> authorities) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.password = null;
        this.authorities = authorities;
        this.enabled = true;
    }

    /**
     * Constructor from Authentication
     *
     * @param authentication Authentication object from Spring Security that contain all information about user
     */


    public User(final Authentication authentication) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
            id = null;
            enabled = false;
            logger.debug("User: get UserDetails in Principal here");
        } else if (principal instanceof Map) {
            logger.debug("User: get Map in Principal here");
            id = ((Map) principal).get("id").toString();
            username = ((Map) principal).get("subject").toString();
            enabled = Boolean.parseBoolean(((Map) principal).get("enabled").toString());

        } else {
            logger.debug("User: Just our string in Principal here");
            username = principal.toString();
            id = null;
            enabled = false;
        }
        password = null;


        authorities = new ArrayList<>();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            authorities.add(authority.getAuthority());
        }
    }

    public String getId() {
        return id;
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

    public boolean getEnabled() {
        return enabled;
    }
}