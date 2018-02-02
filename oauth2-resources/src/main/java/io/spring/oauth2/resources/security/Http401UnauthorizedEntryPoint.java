package io.spring.oauth2.resources.security;

import org.slf4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Returns a 401 error code (Unauthorized) to the client.
 */
@Component
public class Http401UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    private final Logger LOG = getLogger(Http401UnauthorizedEntryPoint.class);

    /**
     * Always returns a 401 error code to the client.
     */
    public void commence(HttpServletRequest req, HttpServletResponse resp, AuthenticationException aex)
            throws IOException, ServletException {
        LOG.debug("Pre-authenticated entry point called. Rejecting access");
        resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied");
    }
}