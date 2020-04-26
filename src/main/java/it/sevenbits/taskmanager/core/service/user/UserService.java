package it.sevenbits.taskmanager.core.service.user;

import it.sevenbits.taskmanager.core.model.user.User;
import it.sevenbits.taskmanager.core.model.user.UserFactory;
import it.sevenbits.taskmanager.core.repository.users.UsersRepository;
import it.sevenbits.taskmanager.web.model.requests.PatchUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for operations with users
 */
@Service
public class UserService {

    private final UsersRepository usersRepository;
    private final UserFactory userFactory;
    private Logger logger;

    /**
     * Default constructor
     * @param usersRepository UsersRepository that store users
     * @param userFactory factory to create User Objects
     */

    public UserService(final UsersRepository usersRepository, final UserFactory userFactory) {
        this.userFactory = userFactory;
        this.usersRepository = usersRepository;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    /**
     * Create new user
     * @param username username of user
     * @param password password of user
     * @return new user with 'USER' role
     */

    public User createNewUser(final String username, final String password) {
        List<String> authorities = new ArrayList<>();
        authorities.add("USER");
        return usersRepository.create(username, password, authorities);
    }

    /**
     * Get current User logged in
     * @return user Object
     */

    public User getCurrentUser() {
        return userFactory.getNewUser(SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     *Update fields of User with PatchUserRequest
     * @param user user object to update
     * @param request PatchUserRequest object with fields to update
     * @return patched user or null
     */

    public User patchUser(final User user, final PatchUserRequest request) {
        try {
            if (request.getEnabled() != null ||
                    (request.getAuthorities() != null)) {
                return userFactory.getNewUser(user.getId(), user.getUsername(), user.getPassword(),
                        notNullOrDefault(request.getAuthorities(), user.getAuthorities()),
                        notNullOrDefault(request.getEnabled(), user.getEnabled()));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;

    }
    private <T> T notNullOrDefault(final T isNull, final T ifNull) {
        if (isNull == null) {
            return ifNull;
        }
        return isNull;
    }
}
