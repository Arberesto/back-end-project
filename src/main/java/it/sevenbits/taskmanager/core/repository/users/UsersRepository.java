package it.sevenbits.taskmanager.core.repository.users;

import it.sevenbits.taskmanager.core.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for interacting with user database
 */

public class UsersRepository {

    private JdbcOperations jdbcOperations;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger;

    private static final String AUTHORITY = "authority";
    private static final String USERNAME = "username";
    private static final  String PASSWORD = "password";

    /**
     * Repository to store User objects
     * @param jdbcOperations JDBC object to interact with database
     * @param passwordEncoder PasswordEncoder to encode passwords
     */

    public UsersRepository(final JdbcOperations jdbcOperations, final PasswordEncoder passwordEncoder) {
        this.jdbcOperations = jdbcOperations;
        this.passwordEncoder = passwordEncoder;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    /**
     * Find user
     * @param username username of user to find
     * @return User if exist or null
     */

    public User findByUserName(final String username) {
        Map<String, Object> rawUser;
        try {
            rawUser = jdbcOperations.queryForMap(
                    "SELECT username, password FROM users u" +
                            " WHERE u.enabled = true AND u.username = ?",
                    username
            );
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }


        List<String> authorities = new ArrayList<>();
        jdbcOperations.query(
                "SELECT username, authority FROM authorities" +
                        " WHERE username = ?",
                resultSet -> {
                    String authority = resultSet.getString(AUTHORITY);
                    authorities.add(authority);
                },
                username
        );

        String password = String.valueOf(rawUser.get("password"));
        return new User(username, password, authorities);
    }

    /**
     * Find all active users in repository
     * @return List of User objects
     */

    public List<User> findAll() {
        HashMap<String, User> users = new HashMap<>();

        for (Map<String, Object> row : jdbcOperations.queryForList(
                "SELECT username, authority FROM authorities a" +
                        " WHERE EXISTS" +
                        " (SELECT * FROM users u WHERE" +
                        " u.username = a.username AND u.enabled = true)")) {

            String username = String.valueOf(row.get(USERNAME));
            String newRole = String.valueOf(row.get(AUTHORITY));
            User user = users.computeIfAbsent(username, name -> new User(name, new ArrayList<>()));
            List<String> roles = user.getAuthorities();
            roles.add(newRole);

        }
        return new ArrayList<>(users.values());
    }

    /**
     * Create new user
     * @param username username of new user
     * @param password password of new user
     * @param authorities authorities of new user
     * @return created User; null if error occurred
     */

    public User create(final String username, final String password, final List<String> authorities) {
        User result = new User(username, password, authorities);
        try {
            int rows = jdbcOperations.update(
                    "INSERT INTO users (username, password, enabled) VALUES (?, ?, ?)",
                    username, passwordEncoder.encode(password), true
            );
            //TODO: remove
            logger.debug(String.format("Password %s encoded as", password, passwordEncoder.encode(password)));
            if (rows <= 0) {
                return null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
        for (String authority : authorities) {
            try {
                int rows = jdbcOperations.update(
                        "INSERT INTO authorities (username, authority) VALUES (?, ?)",
                        username, authority
                );
                if (rows <= 0) {
                    User deletedUser = jdbcOperations.queryForObject(
                            "DELETE FROM users WHERE username = ? RETURNING username, password, enabled",
                            (resultSet, i) -> {
                                String rowName = resultSet.getString("username");
                                String rowPassword = resultSet.getString("password");
                                return new User(rowName, rowPassword, new ArrayList<>());
                            },
                            username);
                    return null;
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                return null;
            }
        }
        return result;
    }


}
