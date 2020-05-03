package it.sevenbits.taskmanager.core.model.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserFactory {


    private static final String ID = "id";
    private static final String SUBJECT = "subject";
    private static final String ENABLED = "enabled";
    private static final Logger LOGGER = LoggerFactory.getLogger(UserFactory.class);

    /**
     * Create User object
     * @param id id of user
     * @param username username of user
     * @param password password of user(encrypted)
     * @param authorities List of authorities of user
     * @param enabled status of user
     * @return User object
     */

    public User getNewUser(final String id, final String username, final String password, final List<String> authorities,
                           final boolean enabled) {
        return new User(id, username, password, authorities, enabled);
    }

    /**
     * Create User object from Authentication
     * @param authentication Authentication object that contain info about user
     * @return User object
     */

    public User getNewUser(final Authentication authentication) {

        String id;
        String username;
        boolean enabled;

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
            id = null;
            enabled = false;
            LOGGER.debug("User: get UserDetails in Principal here");
        } else if (principal instanceof Map) {
            LOGGER.debug("User: get Map in Principal here");
            id = ((Map) principal).get(ID).toString();
            username = ((Map) principal).get(SUBJECT).toString();
            enabled = Boolean.parseBoolean(((Map) principal).get(ENABLED).toString());

        } else {
            LOGGER.debug("User: Just our string in Principal here");
            username = principal.toString();
            id = null;
            enabled = false;
        }


        List<String> authorities = new ArrayList<>();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            authorities.add(authority.getAuthority());
        }

        return new User(id, username, null, authorities, enabled);
    }

    /**
     * Create new User object
     * @param username id of user
     * @param password username of user
     * @param authorities List of authorities of user
     * @return User object
     */

    public User getNewUser(final String username, final String password, final List<String> authorities) {
        return new User(username, password, authorities);
    }

    private String getNewId() {
        return UUID.randomUUID().toString();
    }
}
