package it.sevenbits.taskmanager.web.controllers;

import it.sevenbits.taskmanager.core.model.user.User;
import it.sevenbits.taskmanager.core.repository.users.UsersRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import java.util.Optional;

/**
 *Users Controller to find users
 */

@Controller
@RequestMapping("/users")
public class UsersController {

    private final UsersRepository usersRepository;

    /**
     * Default constructor
     * @param usersRepository repository to store users
     */

    public UsersController(final UsersRepository usersRepository) {

        this.usersRepository = usersRepository;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(usersRepository.findAll());
    }

    /**
     *Response for GET on /users/{username}
     * @param username username of searched user
     * @return 200 if ok, 404 if user not found
     */

    @GetMapping(value = "/{username}")
    @ResponseBody
    public ResponseEntity<User> getUserInfo(final @PathVariable("username") String username) {
        return Optional
                .ofNullable(usersRepository.findByUserName(username))
                .map(user -> ResponseEntity.ok().body(user))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
