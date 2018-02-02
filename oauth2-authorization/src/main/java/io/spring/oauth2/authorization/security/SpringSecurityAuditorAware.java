package io.spring.oauth2.authorization.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import static io.spring.oauth2.authorization.security.SecurityUtils.getCurrentUserLogin;

/**
 * Implementation of AuditorAware based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    public static final String SYSTEM_ACCOUNT = "system";

    @Override
    public String getCurrentAuditor() {
        String userName = getCurrentUserLogin();
        return (userName != null ? userName : SYSTEM_ACCOUNT);
    }
}