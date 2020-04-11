package it.sevenbits.taskmanager.core.service.login;

import it.sevenbits.taskmanager.core.model.user.User;
import it.sevenbits.taskmanager.core.repository.users.UsersRepository;
import it.sevenbits.taskmanager.core.service.login.exceptions.LoginFailedException;
import it.sevenbits.taskmanager.core.service.login.exceptions.UserAlreadyExistsException;
import it.sevenbits.taskmanager.core.service.user.UserService;

import it.sevenbits.taskmanager.web.model.requests.SignInRequest;
import it.sevenbits.taskmanager.web.model.requests.SignUpRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UsersRepository users;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    /**
     * Default constructor
     * @param users repository to store users
     * @param passwordEncoder encoder to work with passwords
     * @param userService service to create users
     */

    public LoginService(final UsersRepository users, final PasswordEncoder passwordEncoder,
                        final UserService userService) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    /**
     * Signin existing user
     * @param request SignInRequest that contains username and password
     * @return User if exist and correct password;Exception if signin failed
     */

    public User signin(final SignInRequest request) {
        User user = users.findByUserName(request.getUsername());
        if (user == null) {
            throw new LoginFailedException("User '" + request.getUsername() + "' not found");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new LoginFailedException("Wrong password");
        }
        return new User(user.getUsername(), user.getAuthorities());
    }

    /**
     * Create new user in system
     * @param request SignUpRequest that contains username and password
     * @return new User with "User" role; exception if this username is already been used
     */

    public User signup(final SignUpRequest request) {
            User user = users.findByUserName(request.getUsername());
            if (user == null) {
                return userService.createNewUser(request.getUsername(), request.getPassword());
            }
            throw new UserAlreadyExistsException("User with that name already exists");
    }

}
