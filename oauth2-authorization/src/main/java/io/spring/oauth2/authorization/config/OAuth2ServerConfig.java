package io.spring.oauth2.authorization.config;

import io.spring.oauth2.authorization.service.PersistentClientDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.inject.Inject;

/**
 * Created by tbolis on 17/2/2016.
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2ServerConfig
        extends AuthorizationServerConfigurerAdapter {

    @Inject
    private TokenStore tokenStore;

    @Inject
    private AuthenticationManager authenticationManager;

    @Inject
    private AuthorizationCodeServices authorizationCodeServices;

    @Inject
    private PersistentClientDetailsService clientDetailsService;

    @Inject
    private DefaultAccessTokenConverter defaultAccessTokenConverter;

    /**
     * Authorization configuration
     *
     * @param endpoints defines the authorization and token endpoints and the token services.
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //@formatter:off
        endpoints
                .tokenStore(tokenStore)
                .authenticationManager(authenticationManager)
                .accessTokenConverter(defaultAccessTokenConverter)
                .authorizationCodeServices(authorizationCodeServices);
        //@formatter:on
    }

    /**
     * Token endpoint security
     *
     * @param security defines the security constraints on the /oauth/token endpoint.
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //@formatter:off
        security
                .allowFormAuthenticationForClients()
                .checkTokenAccess("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')");
        //@formatter:on
    }

    /**
     * Clients Configuration
     *
     * @param clients a configurer that defines the client details service. Client details can be initialized,
     *                or you can just refer to an existing store.
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }
}