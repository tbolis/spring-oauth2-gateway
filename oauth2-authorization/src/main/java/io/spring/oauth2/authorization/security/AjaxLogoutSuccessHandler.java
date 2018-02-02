package io.spring.oauth2.authorization.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang.StringUtils.substringAfter;

/**
 * Spring Security logout handler, specialized for Ajax requests.
 */
@Component
public class AjaxLogoutSuccessHandler
        extends AbstractAuthenticationTargetUrlRequestHandler implements LogoutSuccessHandler {

    public static final String _AUTHENTICATION = "Bearer ";

    @Inject
    private TokenStore tokenStore;

    @Override
    public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication auth) throws IOException, ServletException {
        ofNullable(req.getHeader("authorization")).ifPresent(token -> {
            if (token.startsWith(_AUTHENTICATION)) {
                ofNullable(tokenStore
                        .readAccessToken(substringAfter(token, _AUTHENTICATION)))
                        .ifPresent(tokenStore::removeAccessToken);
            }
        });
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}