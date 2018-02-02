package io.spring.oauth2.resources.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

/**
 * Class representing the Spring Security authenticated user.
 *
 * @see UserDetails
 */
@Getter
public class CustomUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;

    private final Long id;
    private final boolean enabled;
    private final String password;
    private final String username;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final Set<GrantedAuthority> authorities;

    /**
     * @param id
     * @param username
     * @param password
     * @param authorities
     * @param accountNonExpired
     * @param accountNonLocked
     * @param credentialsNonExpired
     * @param enabled
     */
    public CustomUserDetails(Long id, String username, String password, Set<GrantedAuthority> authorities,
                             boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired,
                             boolean enabled) {
        this.id = id;
        this.enabled = enabled;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public boolean isUserInRole(String authority) {
        return authorities.contains(new SimpleGrantedAuthority(authority));
    }
}
