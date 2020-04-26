package it.sevenbits.taskmanager.web.controllers;

import it.sevenbits.taskmanager.core.model.user.User;
import it.sevenbits.taskmanager.core.repository.users.UsersRepository;

import it.sevenbits.taskmanager.core.service.user.UserService;
import it.sevenbits.taskmanager.web.model.requests.PatchUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final UserService userService;

    /**
     * Default constructor
     * @param usersRepository repository to store users
     * @param userService service to work with users
     */

    public UsersController(final UsersRepository usersRepository, final UserService userService) {

        this.userService = userService;
        this.usersRepository = usersRepository;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(usersRepository.findAll());
    }

    /**
     *Response for GET on /users/{userId}
     * @param userId id of searched user
     * @return 200 if ok, 404 if user not found
     */

    @GetMapping(value = "/{userId}")
    @ResponseBody
    public ResponseEntity<User> getUserInfo(final @PathVariable("userId") String userId) {
        return Optional
                .ofNullable(usersRepository.findActiveUser(userId))
                .map(user -> ResponseEntity.ok().body(user))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Response for PATCH on /users/{userId}
     * @param userId id of user
     * @param patchUserRequest body Object that contain JSON with fields to update
     * @return JSON with updated user
     */

    @RequestMapping(path = "/{userId}", method = RequestMethod.PATCH, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity patchUser(final @PathVariable("userId") String userId,
                                    final @RequestBody PatchUserRequest patchUserRequest) {
        User user = usersRepository.findActiveUser(userId);
        if (patchUserRequest.validate()) {
            return Optional
                    .ofNullable(usersRepository.updateUser(userId, userService.patchUser(user, patchUserRequest)))
                    .map(updatedUser -> ResponseEntity.noContent().build())
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
        return ResponseEntity.badRequest().build();
    }
}
