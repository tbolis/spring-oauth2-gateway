package io.spring.oauth2.authorization.security;

import io.spring.oauth2.domain.user.User;
import io.spring.oauth2.domain.user.UserRepository;
import org.slf4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Authenticate a user from the database.
 */
@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final Logger log = getLogger(UserDetailsService.class);

    @Inject
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);
        String lowercaseLogin = login.toLowerCase();
        Optional<User> userFromDatabase = userRepository.findOneByLogin(lowercaseLogin);
        return userFromDatabase.map(user -> {
            if (!user.getActivated()) {
                throw new UserNotActivatedException(format("User %s was not activated", lowercaseLogin));
            }
            Set<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                    .collect(Collectors.toSet());
            return new CustomUserDetails(user.getId(), lowercaseLogin, user.getPassword(), grantedAuthorities, true, true, true, true);
        }).orElseThrow(() -> new UsernameNotFoundException(format("User %s was not found in the database", lowercaseLogin)));
    }
}
