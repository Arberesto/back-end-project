package it.sevenbits.taskmanager.core.repository.users;

import it.sevenbits.taskmanager.core.model.user.User;
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

    private final String AUTHORITY = "authority";
    //private final String USERNAME = "username";
    //private final String PASSWORD = "password";

    public UsersRepository(final JdbcOperations jdbcOperations, final PasswordEncoder passwordEncoder) {
        this.jdbcOperations = jdbcOperations;
        this.passwordEncoder = passwordEncoder;
    }

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

    public List<User> findAll() {
        HashMap<String, User> users = new HashMap<>();

        for (Map<String, Object> row : jdbcOperations.queryForList(
                "SELECT username, authority FROM authorities a" +
                        " WHERE EXISTS" +
                        " (SELECT * FROM users u WHERE" +
                        " u.username = a.username AND u.enabled = true)")) {

            String username = String.valueOf(row.get("username"));
            String newRole = String.valueOf(row.get(AUTHORITY));
            User user = users.computeIfAbsent(username, name -> new User(name, new ArrayList<>()));
            List<String> roles = user.getAuthorities();
            roles.add(newRole);

        }
        return new ArrayList<>(users.values());
    }

    public User create(final String username, final String password, final List<String> authorities) {
        User result = new User(username, password, authorities);
        try {
            int rows = jdbcOperations.update(
                    "INSERT INTO users (username, password, boolean) VALUES (?, ?, ?)",
                    username, passwordEncoder.encode(password), true
            );
            if (rows <= 0) {
                return null;
            }
        } catch (Exception e) {
            //logger.error(e.getMessage());
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
                            "DELETE FROM users WHERE username = ? RETURNING username, password, boolean",
                            (resultSet, i) -> {
                                String rowName = resultSet.getString("username");
                                String rowPassword = resultSet.getString("password");
                                return new User(rowName, rowPassword, new ArrayList<>());
                            },
                            username);
                    return null;
                }
            } catch (Exception e) {
                //logger.error(e.getMessage());
                return null;
            }
        }
        return result;
    }


}
