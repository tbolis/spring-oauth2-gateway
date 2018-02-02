package io.spring.oauth2.authorization.config;

import io.spring.oauth2.authorization.security.AjaxLogoutSuccessHandler;
import io.spring.oauth2.authorization.security.Http401UnauthorizedEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;

import javax.inject.Inject;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig
        extends WebSecurityConfigurerAdapter {

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserDetailsService userDetailsService;

    @Inject
    private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

    @Inject
    private Http401UnauthorizedEntryPoint authenticationEntryPoint;


    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //@formatter:off
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
        //@formatter:on
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
        http
                .requestMatchers()
                .antMatchers("/login", "/oauth/authorize", "/oauth/confirm_access")
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(ajaxLogoutSuccessHandler);
        /*
        // disable csrf
        .and()
            .csrf()
            .disable();
        */
        //@formatter:on
    }

    /**
     * Exposes the AuthenticationManager from configure(AuthenticationManagerBuilder)
     *
     * @return AuthenticationManager
     * @throws Exception
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * By defining this object as a Bean, Spring Security is exposed as SpEL expressions for creating
     * Spring Data queries.
     *
     * @return SecurityEvaluationContextExtension
     */
    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
}