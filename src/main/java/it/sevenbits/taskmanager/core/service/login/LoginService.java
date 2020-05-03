package it.sevenbits.taskmanager.core.service.login;

import it.sevenbits.taskmanager.core.model.user.User;
import it.sevenbits.taskmanager.core.model.user.UserFactory;
import it.sevenbits.taskmanager.core.repository.users.UsersRepository;
import it.sevenbits.taskmanager.core.service.login.exceptions.InvalidBodyException;
import it.sevenbits.taskmanager.core.service.login.exceptions.LoginFailedException;
import it.sevenbits.taskmanager.core.service.login.exceptions.UserAlreadyExistsException;
import it.sevenbits.taskmanager.core.service.user.UserService;

import it.sevenbits.taskmanager.web.model.requests.SignInRequest;
import it.sevenbits.taskmanager.web.model.requests.SignUpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UsersRepository users;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final UserFactory userFactory;
    private final Logger logger;

    /**
     * Default constructor
     *
     * @param users           repository to store users
     * @param passwordEncoder encoder to work with passwords
     * @param userService     service to create users
     * @param userFactory     factory to get User objects
     */

    public LoginService(final UsersRepository users, final PasswordEncoder passwordEncoder,
                        final UserService userService, final UserFactory userFactory) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.userFactory = userFactory;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    /**
     * Signin existing user
     *
     * @param request SignInRequest that contains username and password
     * @return User if exist and correct password;Exception if signin failed
     */

    public User signin(final SignInRequest request) {
        logger.debug(request.toString());
        try {
            User user = users.findByUserName(request.getUsername());
            if (user != null) {
                if (user.getEnabled()) {
                    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                        throw new LoginFailedException("Wrong password");
                    }
                    return userFactory.getNewUser(user.getId(), user.getUsername(), null, user.getAuthorities(),
                            user.getEnabled());
                }
            }
            throw new LoginFailedException("User '" + request.getUsername() + "' not found");
        } catch (Exception e) {
            logger.error(String.format("Error in signin: %s", e.getMessage()));
            throw new InvalidBodyException("Invalid signin request");
        }
    }

    /**
     * Create new user in system
     *
     * @param request SignUpRequest that contains username and password
     * @return new User with "User" role; exception if this username is already been used
     */

    public User signup(final SignUpRequest request) {
        try {
            User user = users.findByUserName(request.getUsername());
            if (user == null) {
                user = userService.createNewUser(request.getUsername(), request.getPassword());
                if (user == null) {
                    logger.warn("Warning: user wasn't created");
                }
                return user;
            }
            throw new UserAlreadyExistsException("User with that name already exists");
        } catch (Exception e) {
            logger.error(String.format("Error in signup: %s", e.getMessage()));
            throw new InvalidBodyException("Invalid signup request");
        }
    }

}
