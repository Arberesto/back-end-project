package it.sevenbits.taskmanager.web.controllers;

import it.sevenbits.taskmanager.core.model.user.User;
import it.sevenbits.taskmanager.core.service.login.exceptions.LoginFailedException;
import it.sevenbits.taskmanager.core.service.login.LoginService;
import it.sevenbits.taskmanager.core.service.login.exceptions.UserAlreadyExistsException;
import it.sevenbits.taskmanager.web.model.requests.SignInRequest;
import it.sevenbits.taskmanager.web.model.requests.SignInResponse;
import it.sevenbits.taskmanager.web.model.requests.SignUpRequest;
import it.sevenbits.taskmanager.web.security.JwtTokenService;
import it.sevenbits.taskmanager.web.security.Token;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 *  Performs login action.
 */


@Controller
public class CookieLoginController implements LoginController {
    private final LoginService loginService;
    private final JwtTokenService tokenService;
    private final Logger logger;

    /**
     * Controller default constructor
     * @param loginService service for signin/signup operations
     * @param tokenService service for token creation
     */

    public CookieLoginController(final LoginService loginService,
                                 @Qualifier("jwtTokenService") final JwtTokenService tokenService) {
        this.loginService = loginService;
        this.tokenService = tokenService;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    /**
     * Response for /signin POST request
     * @param request SignInRequest object contains username and password
     * @param response HttpServletResponse object used to set Cookie
     * @return 200 - Token object inside SignInResponse; exception if things goes wrong
     */

    @PostMapping(path = "/signin")
    @ResponseBody
    public ResponseEntity<SignInResponse> signin(@RequestBody final SignInRequest request,
                                                 final HttpServletResponse response) {
        try {
            User user = loginService.signin(request);
            Token token = new Token(tokenService.createToken(user));
            logger.debug("Token was generated");
            Cookie cookie = new Cookie("accessToken", token.getToken());
            cookie.setHttpOnly(true);
            cookie.setMaxAge(tokenService.getTokenExpiredIn());
            response.addCookie(cookie);
            logger.debug("Ready to response to signin");
            return ResponseEntity.ok(new SignInResponse(token));
        } catch (LoginFailedException e) {
            logger.error("Invalid login or password");
            return ResponseEntity.ok(new SignInResponse(null));
        }

    }

    /**
     * Response for /signup POST request
     * @param request SignUpRequest object contains username and password
     * @return 204 if created, 209 if user already exist
     */

    @RequestMapping(path = "/signup", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseEntity signup(@RequestBody final SignUpRequest request) {
        try {
            loginService.signup(request);
            return ResponseEntity.noContent().build();
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        }


    }
}

