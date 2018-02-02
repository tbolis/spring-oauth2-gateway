package io.spring.oauth2.authorization.rest.filter;

import org.slf4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Component
@Order(HIGHEST_PRECEDENCE)
public class CORSFilter extends OncePerRequestFilter {

    private final Logger log = getLogger(CORSFilter.class);

    public CORSFilter() {
        log.info("CORSFilter init");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws ServletException, IOException {
        log.debug("Invoking CORSFilter from " + req.getPathInfo());
        resp.addHeader("Access-Control-Allow-Origin", "*");
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            resp.setHeader("Access-Control-Max-Age", "3600");
            resp.setHeader("Access-Control-Allow-Methods", "POST,PUT,GET,DELETE");
            resp.setHeader("Access-Control-Allow-Headers", "content-type,access-control-request-headers,access-control-request-method,accept,origin,authorization,x-requested-with");
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, resp);
        }
    }
}