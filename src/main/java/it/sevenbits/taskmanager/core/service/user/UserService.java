package it.sevenbits.taskmanager.core.service.user;

import it.sevenbits.taskmanager.core.model.user.User;
import it.sevenbits.taskmanager.core.repository.users.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for operations with users
 */
@Service
public class UserService {

    private final UsersRepository usersRepository;

    /**
     * Default constructor
     * @param usersRepository UsersRepository that store users
     */

    public UserService(final UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
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
}
