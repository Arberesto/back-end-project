package it.sevenbits.taskmanager.web.controllers;

import it.sevenbits.taskmanager.core.model.user.User;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/whoami")
public class WhoamiController {

    /**
     * Responce for GET on /whoami
     * @return 200 if ok
     */

    @GetMapping
    @ResponseBody
    public ResponseEntity<User> get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(new User(authentication));
    }
}
