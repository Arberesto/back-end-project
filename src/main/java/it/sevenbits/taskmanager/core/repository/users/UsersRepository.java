package it.sevenbits.taskmanager.core.repository.users;

import it.sevenbits.taskmanager.core.model.user.User;
import it.sevenbits.taskmanager.core.model.user.UserFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Class for interacting with user database
 */

public class UsersRepository {

    private JdbcOperations jdbcOperations;
    private UserFactory factory;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger;

    private static final String ID = "id";
    private static final String AUTHORITY = "authority";
    private static final String USERNAME = "username";
    private static final  String PASSWORD = "password";
    private static final  String ENABLED = "enabled";

    /**
     * Repository to store User objects
     * @param jdbcOperations JDBC object to interact with database
     * @param passwordEncoder PasswordEncoder to encode passwords
     * @param userFactory userFactory to get User objects
     */

    public UsersRepository(final JdbcOperations jdbcOperations, final PasswordEncoder passwordEncoder,
                           final UserFactory userFactory) {
        this.jdbcOperations = jdbcOperations;
        this.passwordEncoder = passwordEncoder;
        this.factory = userFactory;
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
                    "SELECT id, password, enabled FROM users u" +
                            " WHERE u.enabled = true AND u.username = ?",
                    username
            );
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }

        String id = String.valueOf(rawUser.get("id"));
        boolean enabled = Boolean.parseBoolean(rawUser.get(ENABLED).toString());
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

        String password = String.valueOf(rawUser.get(PASSWORD));
        return factory.getNewUser(id, username, password, authorities, enabled);
    }

    /**
     * Find user by id
     * @param id of user to find
     * @return User if exist or null
     */

    public User findActiveUser(final String id) {
        Map<String, Object> rawUser;
        try {
            rawUser = jdbcOperations.queryForMap(
                    "SELECT username, password, enabled FROM users u" +
                            " WHERE u.enabled = true AND u.id = ?",
                    id
            );
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }

        boolean enabled = Boolean.parseBoolean(rawUser.get(ENABLED).toString());
        List<String> authorities = new ArrayList<>();
        String username = String.valueOf(rawUser.get(USERNAME));
        jdbcOperations.query(
                "SELECT username, authority FROM authorities" +
                        " WHERE username = ?",
                resultSet -> {
                    String authority = resultSet.getString(AUTHORITY);
                    authorities.add(authority);
                },
                username
        );

        String password = String.valueOf(rawUser.get(PASSWORD));
        return factory.getNewUser(id, username, password, authorities, enabled);
    }

    /**
     * Find all active users in repository
     * @return List of User objects
     */

    public List<User> findAll() {
        HashMap<String, User> users = new HashMap<>();

        for (Map<String, Object> row : jdbcOperations.queryForList(
                "SELECT users.id, users.username, authorities.authority FROM users" +
                        " INNER JOIN authorities ON users.username = authorities.username" +
                        " WHERE users.enabled = true")) {

            String id = String.valueOf(row.get(ID));
            boolean enabled = Boolean.parseBoolean(row.get(ENABLED).toString());
            String username = String.valueOf(row.get(USERNAME));
            String newRole = String.valueOf(row.get(AUTHORITY));
            User user = users.computeIfAbsent(username, name -> factory.getNewUser(id, name, null,
                    new ArrayList<>(), enabled));
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
        User result = factory.getNewUser(username, password, authorities);
        try {
            int rows = jdbcOperations.update(
                    "INSERT INTO users (id,username, password, enabled) VALUES (?, ?, ?, ?)",
                    getNewId(), username, passwordEncoder.encode(password), true
            );
            //logger.debug(String.format("Password %s encoded as %s", password, passwordEncoder.encode(password)));
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
                                return factory.getNewUser(rowName, rowPassword, new ArrayList<>());
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

    /**
     * Update User object
     * @param id id of User to update
     * @param changedUser updated User object
     * @return updated User or null if error occurs
     */

    public User updateUser(final String id, final User changedUser) {
        if (changedUser != null) {
            try {
                //TODO:Should use BEGIN to create transaction

                int rowsInsert = jdbcOperations.update(
                        "INSERT INTO users(id, username, password, enabled) VALUES (?, ?, ?, ?) " +
                                "ON CONFLICT(id) DO UPDATE SET enabled = ?",
                        changedUser.getId(), changedUser.getUsername(), changedUser.getPassword(),
                        changedUser.getAuthorities(), changedUser.getAuthorities()
                );
                if (rowsInsert > 0) {
                    return changedUser;
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                return null;
            }
        }
        return null;
    }

    /**
     * Generate id for new user
     * @return UUID as String
     */

    private UUID getNewId() {
        return UUID.randomUUID();
    }

}
