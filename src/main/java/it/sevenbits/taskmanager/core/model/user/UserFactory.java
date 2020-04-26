package it.sevenbits.taskmanager.core.model.user;

import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

public class UserFactory {

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
        return new User(authentication);
    }

    /**
     * Create new User object
     * @param username id of user
     * @param password username of user
     * @param authorities List of authorities of user
     * @return User object
     */

    public User getNewUser (final String username, final String password, final List<String> authorities) {
        return new User(username, password, authorities);
    }

    private String getNewId() {
        return UUID.randomUUID().toString();
    }
}
