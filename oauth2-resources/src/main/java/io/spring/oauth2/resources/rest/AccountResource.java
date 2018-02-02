package io.spring.oauth2.resources.rest;

import com.codahale.metrics.annotation.Timed;
import io.spring.oauth2.domain.user.User;
import io.spring.oauth2.domain.user.UserRepository;
import io.spring.oauth2.resources.rest.dto.KeyAndPasswordDTO;
import io.spring.oauth2.resources.rest.dto.UserDTO;
import io.spring.oauth2.resources.security.SecurityUtils;
import io.spring.oauth2.resources.service.MailService;
import io.spring.oauth2.resources.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Inject
    private MailService mailService;

    /**
     * POST  /register -> register the user.
     */
    @Timed
    @RequestMapping(value = "/register", method = POST, produces = TEXT_PLAIN_VALUE)
    public ResponseEntity<?> registerAccount(@Valid @RequestBody UserDTO userDTO, HttpServletRequest request) {
        return userRepository.findOneByLogin(userDTO.getLogin())
                .map(user -> new ResponseEntity<>("login already in use", BAD_REQUEST))
                .orElseGet(() -> userRepository.findOneByEmail(userDTO.getEmail())
                        .map(user -> new ResponseEntity<>("e-mail address already in use", BAD_REQUEST))
                        .orElseGet(() -> {
                            User user = userService.createUserInformation(userDTO.getLogin(), userDTO.getPassword(),
                                    userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail().toLowerCase(),
                                    userDTO.getLangKey());
                            String baseUrl = request.getScheme() + // "http"
                                    "://" +                        // "://"
                                    request.getServerName() +      // "myhost"
                                    ":" +                          // ":"
                                    request.getServerPort();       // "80"

                            mailService.sendActivationEmail(user, baseUrl);
                            return new ResponseEntity<>(CREATED);
                        })
                );
    }

    /**
     * GET  /activate -> activate the registered user.
     */
    @Timed
    @RequestMapping(value = "/activate", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> activateAccount(@RequestParam(value = "key") String key) {
        return ofNullable(userService.activateRegistration(key))
                .map(user -> new ResponseEntity<String>(OK))
                .orElse(new ResponseEntity<>(INTERNAL_SERVER_ERROR));
    }

    /**
     * GET  /authenticate -> check if the user is authenticated, and return its login.
     */
    @RequestMapping(value = "/authenticate",
            method = GET,
            produces = APPLICATION_JSON_VALUE)
    @Timed
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * GET  /account -> get the current user.
     */
    @Timed
    @RequestMapping(value = "/account", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getAccount() {
        return ofNullable(userService.getUserWithAuthorities())
                .map(user -> new ResponseEntity<>(new UserDTO(user), OK))
                .orElse(new ResponseEntity<>(INTERNAL_SERVER_ERROR));
    }

    /**
     * POST  /account -> update the current user information.
     */
    @Timed
    @RequestMapping(value = "/account", method = POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveAccount(@RequestBody UserDTO userDTO) {
        return userRepository
                .findOneByLogin(userDTO.getLogin())
                .filter(u -> u.getLogin().equals(SecurityUtils.getCurrentUserLogin()))
                .map(u -> {
                    userService.updateUserInformation(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
                            userDTO.getLangKey());
                    return new ResponseEntity<String>(OK);
                })
                .orElseGet(() -> new ResponseEntity<>(INTERNAL_SERVER_ERROR));
    }

    /**
     * POST  /change_password -> changes the current user's password
     */
    @Timed
    @RequestMapping(value = "/account/change_password", method = POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(@RequestBody String password) {
        if (!checkPasswordLength(password)) {
            return new ResponseEntity<>("Incorrect password", BAD_REQUEST);
        }
        userService.changePassword(password);
        return new ResponseEntity<>(OK);
    }

    @Timed
    @RequestMapping(value = "/account/reset_password/init", method = POST, produces = TEXT_PLAIN_VALUE)
    public ResponseEntity<?> requestPasswordReset(@RequestBody String mail, HttpServletRequest request) {
        return userService.requestPasswordReset(mail)
                .map(user -> {
                    String baseUrl = request.getScheme() +
                            "://" +
                            request.getServerName() +
                            ":" +
                            request.getServerPort();
                    mailService.sendPasswordResetMail(user, baseUrl);
                    return new ResponseEntity<>("e-mail was sent", OK);
                }).orElse(new ResponseEntity<>("e-mail address not registered", BAD_REQUEST));
    }

    @RequestMapping(value = "/account/reset_password/finish",
            method = POST,
            produces = APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> finishPasswordReset(@RequestBody KeyAndPasswordDTO keyAndPassword) {
        if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
            return new ResponseEntity<>("Incorrect password", BAD_REQUEST);
        }
        return userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey())
                .map(user -> new ResponseEntity<String>(OK)).orElse(new ResponseEntity<>(INTERNAL_SERVER_ERROR));
    }

    private boolean checkPasswordLength(String password) {
        return (!StringUtils.isEmpty(password) &&
                password.length() >= UserDTO.PASSWORD_MIN_LENGTH &&
                password.length() <= UserDTO.PASSWORD_MAX_LENGTH);
    }
}
