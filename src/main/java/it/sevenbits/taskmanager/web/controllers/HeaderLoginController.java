package it.sevenbits.taskmanager.web.controllers;

import it.sevenbits.taskmanager.core.model.user.User;
import it.sevenbits.taskmanager.core.service.login.LoginService;
import it.sevenbits.taskmanager.core.service.login.exceptions.InvalidBodyException;
import it.sevenbits.taskmanager.core.service.login.exceptions.LoginFailedException;
import it.sevenbits.taskmanager.core.service.login.exceptions.UserAlreadyExistsException;
import it.sevenbits.taskmanager.web.model.requests.SignInRequest;
import it.sevenbits.taskmanager.web.model.requests.SignUpRequest;
import it.sevenbits.taskmanager.web.model.responce.SignInResponse;
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

/**
 *  Performs login action.
 */

@Controller
public class HeaderLoginController implements LoginController {

    private final LoginService loginService;
    private final JwtTokenService tokenService;
    private final Logger logger;

    /**
     * Controller default constructor
     * @param loginService service for signin/signup operations
     * @param tokenService service for token creation
     */

    public HeaderLoginController(final LoginService loginService,
                                 @Qualifier("jwtTokenService") final JwtTokenService tokenService) {
        this.loginService = loginService;
        this.tokenService = tokenService;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    /**
     * Response for /signin POST request
     * @param request SignInRequest object contains username and password
     * @return 200 - Token object inside SignInResponse; exception if things goes wrong
     */

    @PostMapping(path = "/signin")
    @ResponseBody
    public ResponseEntity<SignInResponse> signin(@RequestBody final SignInRequest request) {
        try {
            User user = loginService.signin(request);
            Token token = new Token("");
            if (user != null) {
                token = new Token(tokenService.createToken(user));
                logger.debug("Token was generated");

            }
            logger.debug("Ready to response to signin");
            return ResponseEntity.ok(new SignInResponse(token));
        } catch (LoginFailedException e) {
            logger.error("Invalid login or password");
            //return ResponseEntity.ok(new SignInResponse(new Token("")));
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (InvalidBodyException e1) {
            logger.error("Invalid body");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
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
        } catch (InvalidBodyException e) {
            logger.error("Invalid input");
            //return ResponseEntity.ok(new SignInResponse(new Token("")));
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


    }
}
