package io.spring.oauth2.authorization.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = AppProperties.PREFIX, ignoreUnknownFields = false)
public class AppProperties {

    public static final String PREFIX = "config";

    @Getter
    private final OAuth oauth = new OAuth();

    @Getter
    private final Mail mail = new Mail();

    @Getter
    @Setter
    public static class Mail {
        private String from = "noreply@localhost";
    }

    @Getter
    @Setter
    public static class OAuth {
        private String clientId = "essauthapp";
        private String secret = "mySecretOAuthSecret";
        private int tokenValidityInSeconds = 1000;
        private RememberMe rememberMe = new RememberMe();

        @Getter
        @Setter
        public static class RememberMe {
            private String key = "20f7735c39d10ba97fadd184e1232182cf7e0c21c";
        }
    }
}
