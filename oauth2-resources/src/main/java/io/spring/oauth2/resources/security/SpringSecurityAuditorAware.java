package io.spring.oauth2.resources.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import static io.spring.oauth2.common.config.Constants.SYSTEM_ACCOUNT;

/**
 * Implementation of AuditorAware based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public String getCurrentAuditor() {
        String userName = SecurityUtils.getCurrentUserLogin();
        return (userName != null ? userName : SYSTEM_ACCOUNT);
    }
}