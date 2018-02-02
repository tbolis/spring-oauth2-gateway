package io.spring.oauth2.resources.rest;

import com.codahale.metrics.annotation.Timed;
import io.spring.oauth2.domain.user.Authority;
import io.spring.oauth2.domain.user.AuthorityRepository;
import io.spring.oauth2.domain.user.User;
import io.spring.oauth2.domain.user.UserRepository;
import io.spring.oauth2.resources.rest.dto.ManagedUserDTO;
import io.spring.oauth2.resources.rest.util.HeaderUtil;
import io.spring.oauth2.resources.rest.util.PaginationUtil;
import io.spring.oauth2.resources.security.AuthoritiesConstants;
import io.spring.oauth2.resources.service.UserService;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * REST controller for managing users.
 * <p>
 * <p>This class accesses the User entity, and needs to fetch its collection of authorities.</p>
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * </p>
 * <p>
 * We use a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </p>
 * <p>Another option would be to have a specific JPA entity graph to handle this case.</p>
 */
@RestController
@RequestMapping("/api")
public class UserResource {
    private final Logger LOG = getLogger(UserResource.class);

    private UserService userService;
    private UserRepository userRepository;
    private PasswordEncoder encoder;
    private AuthorityRepository authorityRepository;

    @Inject
    public UserResource(UserService service, UserRepository userRepo,
                        PasswordEncoder encoder, AuthorityRepository authorityRepo) {
        this.encoder = encoder;
        this.userService = service;
        this.userRepository = userRepo;
        this.authorityRepository = authorityRepo;
    }

    /**
     * POST  /users -> Create a new user.
     */
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    @RequestMapping(value = "/users", method = POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@RequestBody User user) throws URISyntaxException {
        LOG.debug("REST request to save new User : {}", user);

        if (user.getId() != null) {
            return badRequest().header("Failure", "A new user cannot already have an ID").body(null);
        }

        if (user.getPassword() == null) {
            return badRequest().header("Failure", "Password is missing").body(null);
        }

        user.setPassword(encoder.encode(user.getPassword()));

        User result = userRepository.save(user);
        return created(new URI("/api/users/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("user", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /users -> Updates an existing User.
     */
    @Timed
    @Transactional
    @Secured(AuthoritiesConstants.ADMIN)
    @RequestMapping(value = "/users", method = PUT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ManagedUserDTO> updateUser(@RequestBody ManagedUserDTO managedUserDTO) throws URISyntaxException {
        LOG.debug("REST request to update User : {}", managedUserDTO);
        return of(userRepository.findOne(managedUserDTO.getId()))
                .map(user -> {
                    // Mandatory fields
                    user.setActivated(managedUserDTO.isActivated());
                    // Optional fields
                    ofNullable(managedUserDTO.getLogin()).ifPresent(user::setLogin);
                    ofNullable(managedUserDTO.getFirstName()).ifPresent(user::setFirstName);
                    ofNullable(managedUserDTO.getLastName()).ifPresent(user::setLastName);
                    ofNullable(managedUserDTO.getEmail()).ifPresent(user::setEmail);
                    ofNullable(managedUserDTO.getLangKey()).ifPresent(user::setLangKey);

                    ofNullable(managedUserDTO.getAuthorities()).ifPresent(a -> {
                        final Set<Authority> authorities = user.getAuthorities();
                        authorities.clear();
                        a.stream().forEach(authority -> authorities.add(authorityRepository.findOne(authority)));
                    });

                    ofNullable(managedUserDTO.getPassword()).ifPresent(pass ->
                            user.setPassword(encoder.encode(managedUserDTO.getPassword())));

                    return ok()
                            .headers(HeaderUtil.createEntityUpdateAlert("user", managedUserDTO.getLogin()))
                            .body(new ManagedUserDTO(userRepository.findOne(managedUserDTO.getId())));
                })
                .orElseGet(() -> new ResponseEntity<>(INTERNAL_SERVER_ERROR));
    }


    /**
     * PUT  /users -> Updates an existing User.
     */
    @Timed
    @Transactional
    @Secured(AuthoritiesConstants.ADMIN)
    @RequestMapping(value = "/users", method = DELETE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteUser(@PathVariable String login) throws URISyntaxException {
        LOG.debug("REST request to delete User : {}", login);
        return userRepository.findOneByLogin(login)
                .map(user -> {
                    userRepository.delete(user);
                    return new ResponseEntity<>(OK);
                }).orElse(new ResponseEntity<>(NOT_FOUND));
    }

    /**
     * GET  /users -> get all users.
     */
    @Timed
    @Transactional(readOnly = true)
    @RequestMapping(value = "/users", method = GET, params = {"page", "size"}, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ManagedUserDTO>> getAllUsers(@RequestParam("page") int page,
                                                            @RequestParam("size") int size) throws URISyntaxException {
        Page<User> p = userRepository.findAll(new PageRequest(page, size));

        // TODO add total for paging rendering, headers do not work

        p.getTotalElements();
        p.getTotalPages();

        List<ManagedUserDTO> managedUserDTOs
                = p.getContent().stream().map(ManagedUserDTO::new).collect(Collectors.toList());

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(p, "/api/users");

        return new ResponseEntity<>(managedUserDTOs, headers, OK);
    }

    /**
     * GET  /users/:login -> get the "login" user.
     */
    @Timed
    @RequestMapping(value = "/users/{login}", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ManagedUserDTO> getUser(@PathVariable String login) {
        LOG.debug("REST request to get User : {}", login);
        return userService.getUserWithAuthoritiesByLogin(login).map(ManagedUserDTO::new)
                .map(managedUserDTO -> new ResponseEntity<>(managedUserDTO, OK))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }
}
