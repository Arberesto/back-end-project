package it.sevenbits.taskmanager.web.controllers;

import it.sevenbits.taskmanager.core.model.user.User;

import it.sevenbits.taskmanager.core.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/whoami")
public class WhoamiController {

    private UserService userService;

    /**
     * Default constructor
     * @param userService serice to work with users
     */

    public WhoamiController(final UserService userService) {
        this.userService = userService;
    }

    /**
     * Responce for GET on /whoami
     * @return 200 if ok
     */

    @GetMapping
    @ResponseBody
    public ResponseEntity<User> get() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }
}
