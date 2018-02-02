package io.spring.oauth2.resources.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = AppProperties.PREFIX, ignoreUnknownFields = false)
public class AppProperties {

    public static final String PREFIX = "config";
    @Getter
    private final Mail mail = new Mail();
    @Getter
    public RemoteTokenService tokenService = new RemoteTokenService();

    @Getter
    @Setter
    public static class RemoteTokenService {
        private String clientId = "client";
        private String clientSecret = "secret";
        private String checkTokenUrl = "http://localhost:8200/oauth/check_token";
    }

    @Getter
    @Setter
    public static class Mail {
        private String from = "noreply@localhost";
    }
}