package br.com.leonardosr.auth.service;

import br.com.leonardosr.auth.config.KeycloakProperties;
import br.com.leonardosr.auth.dto.TokenResponse;
import br.com.leonardosr.auth.dto.UsuarioRequest;
import br.com.leonardosr.auth.exception.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.WebApplicationException;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakService.class);

    private final KeycloakProperties keycloakProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    public String login(String username, String password) {
        try {
            logger.info("Tentativa de login para usuário: {}", username);
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(keycloakProperties.getAuthServerUrl())
                    .realm(keycloakProperties.getRealm())
                    .clientId(keycloakProperties.getResource())
                    .clientSecret(keycloakProperties.getCredentials().getSecret())
                    .username(username)
                    .password(password)
                    .build();

            String token = keycloak.tokenManager().getAccessTokenString();
            logger.info("Login bem-sucedido para usuário: {}", username);
            return token;
        } catch (Exception e) {
            logger.error("Erro ao realizar login para usuário {}: {}", username, e.getMessage());
            throw new AuthenticationException("Falha na autenticação: credenciais inválidas");
        }
    }

    public String register(UsuarioRequest request) {
        try {
            logger.info("Iniciando registro de novo usuário com email: {}", request.getEmail());
            
            Keycloak adminClient = createAdminClient();
            RealmResource realmResource = adminClient.realm(keycloakProperties.getRealm());
            UsersResource usersResource = realmResource.users();

            // Verifica se o usuário já existe
            if (!usersResource.search(request.getEmail()).isEmpty()) {
                logger.warn("Tentativa de registro com email já existente: {}", request.getEmail());
                throw new AuthenticationException("Email já cadastrado");
            }

            UserRepresentation user = createUserRepresentation(request);
            
            try {
                usersResource.create(user);
                logger.info("Usuário registrado com sucesso: {}", request.getEmail());
                return login(request.getEmail(), request.getSenha());
            } catch (WebApplicationException e) {
                logger.error("Erro ao criar usuário no Keycloak: {}", e.getMessage());
                throw new AuthenticationException("Erro ao criar usuário: " + e.getMessage());
            }
        } catch (Exception e) {
            logger.error("Erro no processo de registro: {}", e.getMessage());
            throw new AuthenticationException("Erro no registro: " + e.getMessage());
        }
    }

    private Keycloak createAdminClient() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakProperties.getAuthServerUrl())
                .realm(keycloakProperties.getAdmin().getRealm())
                .clientId(keycloakProperties.getAdmin().getClientId())
                .username(keycloakProperties.getAdmin().getUsername())
                .password(keycloakProperties.getAdmin().getPassword())
                .build();
    }

    private UserRepresentation createUserRepresentation(UsuarioRequest request) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(request.getEmail());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getNome());
        user.setEmailVerified(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.getSenha());
        credential.setTemporary(false);

        user.setCredentials(Collections.singletonList(credential));
        user.setRealmRoles(List.of("USER"));

        return user;
    }

    public TokenResponse getToken(String username, String password) {
        String tokenUrl = String.format("%s/realms/%s/protocol/openid-connect/token", authServerUrl, realm);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("username", username);
        map.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<TokenResponse> response = restTemplate.postForEntity(tokenUrl, request, TokenResponse.class);
        return response.getBody();
    }

    public TokenResponse refreshToken(String refreshToken) {
        String tokenUrl = String.format("%s/realms/%s/protocol/openid-connect/token", authServerUrl, realm);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "refresh_token");
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<TokenResponse> response = restTemplate.postForEntity(tokenUrl, request, TokenResponse.class);
        return response.getBody();
    }

    public void logout(String refreshToken) {
        String logoutUrl = String.format("%s/realms/%s/protocol/openid-connect/logout", authServerUrl, realm);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        restTemplate.postForEntity(logoutUrl, request, Void.class);
    }
} 