package it.sevenbits.taskmanager.core.service.login;

import it.sevenbits.taskmanager.core.model.user.User;
import it.sevenbits.taskmanager.core.repository.users.UsersRepository;
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

    public LoginService(final UsersRepository users, final PasswordEncoder passwordEncoder,
                        final UserService userService) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    public User login(final SignInRequest request) {
        User user = users.findByUserName(request.getUsername());
        if (user == null) {
            throw new LoginFailedException("User '" + request.getUsername() + "' not found");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new LoginFailedException("Wrong password");
        }
        return new User(user.getUsername(), user.getAuthorities());
    }

    public User signup(final SignUpRequest request) {
            User user = users.findByUserName(request.getUsername());
            if (user == null) {
                return userService.createNewUser(request.getUsername(), request.getPassword());
            }
            throw new UserAlreadyExistsException("User with that name already exists");
    }

}
