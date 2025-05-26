package br.com.leonardosr.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {
    private String authServerUrl;
    private String realm;
    private String resource;
    private Credentials credentials;
    private Admin admin;
    private boolean publicClient;
    private boolean bearerOnly;
    private String sslRequired;

    @Data
    public static class Credentials {
        private String secret;
    }

    @Data
    public static class Admin {
        private String username;
        private String password;
        private String realm;
        private String clientId;
    }
} 