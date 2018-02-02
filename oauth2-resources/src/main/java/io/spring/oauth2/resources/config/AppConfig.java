package io.spring.oauth2.resources.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

@Configuration
@EnableConfigurationProperties({AppProperties.class})
public class AppConfig {

    @Bean
    public AccessTokenConverter accessTokenConverter() {
        // http://stackoverflow.com/questions/26291855/not-getting-client-authority-role-while-using-remotetokenservice
        return new DefaultAccessTokenConverter();
    }

    @Bean
    public ResourceServerTokenServices remoteTokenServices(AppProperties properties) {
        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        remoteTokenServices.setClientId(properties.getTokenService().getClientId());
        remoteTokenServices.setClientSecret(properties.getTokenService().getClientSecret());
        remoteTokenServices.setCheckTokenEndpointUrl(properties.getTokenService().getCheckTokenUrl());
        remoteTokenServices.setAccessTokenConverter(accessTokenConverter());
        return remoteTokenServices;
    }
}