package io.spring.oauth2.authorization.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.inject.Singleton;
import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties({AppProperties.class})
public class AppConfig {

    /**
     * Extract a token value from an incoming request without authentication.
     *
     * @return Bearer type TokenExtractor instance
     */
    @Bean
    public TokenExtractor tokenExtractor() {
        return new BearerTokenExtractor();
    }

    /**
     * Service  for issuing and storing authorization codes.
     *
     * @return an InMemoryAuthorizationCodeServices
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Bean
    @Singleton
    public TokenStore tokenStore(DataSource dataSource) {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    public DefaultAccessTokenConverter defaultAccessTokenConverter() {
        return new DefaultAccessTokenConverter();
    }

}