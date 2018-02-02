package io.spring.oauth2.resources.config;

import io.spring.oauth2.resources.security.AuthoritiesConstants;
import io.spring.oauth2.resources.security.Http401UnauthorizedEntryPoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import javax.inject.Inject;

import static org.springframework.http.HttpMethod.OPTIONS;

@Configuration
@EnableResourceServer
public class OAuth2ServerConfig
        extends ResourceServerConfigurerAdapter {

    public static String RESOURCE_ID = "OAuth2Server";

    @Inject
    private ResourceServerTokenServices tokenServices;

    @Inject
    private Http401UnauthorizedEntryPoint authenticationEntryPoint;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        //@formatter:off
        resources
                .stateless(true)
                .resourceId(RESOURCE_ID)
                .tokenServices(tokenServices);
        //@formatter:on
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                // Since we want the protected resources to be accessible in the UI as well we need
                // session creation to be allowed (it's disabled by default in 2.0.6)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .requestMatchers()
                .antMatchers("/api/**", "/user")
                .and()
                .authorizeRequests()
                .antMatchers(OPTIONS, "/**").permitAll()
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/api/register").permitAll()
                .antMatchers("/api/logs/**").hasAnyAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/api/**").authenticated()
                .antMatchers("/metrics/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/health/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/trace/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/dump/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/shutdown/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/beans/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/configprops/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/info/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/autoconfig/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/env/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/trace/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/liquibase/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/api-docs/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/protected/**").authenticated()
                .antMatchers("/protected/**").authenticated()
                .antMatchers("/user").access("#oauth2.isOAuth() and (hasRole('ROLE_ADMIN') or hasRole('ROLE_USER'))");
        // @formatter:on
    }
}