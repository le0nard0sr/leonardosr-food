package br.com.leonardosr.auth.integration;

import br.com.leonardosr.auth.config.TestSecurityConfig;
import br.com.leonardosr.auth.dto.LoginRequest;
import br.com.leonardosr.auth.dto.UsuarioRequest;
import br.com.leonardosr.auth.service.KeycloakService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class KeycloakIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private KeycloakService keycloakService;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Test
    void shouldRegisterAndLoginWithMockedKeycloak() throws Exception {
        // Dados do usuário de teste
        UsuarioRequest registerRequest = new UsuarioRequest(
            "Usuário de Teste",
            "test@example.com",
            "password123"
        );
        String registerToken = "register-token";
        when(keycloakService.register(any(UsuarioRequest.class))).thenReturn(registerToken);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(registerToken));

        // Login
        LoginRequest loginRequest = new LoginRequest(
            "test@example.com",
            "password123"
        );
        String loginToken = "login-token";
        when(keycloakService.login(anyString(), anyString())).thenReturn(loginToken);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(loginToken));
    }

    @Test
    void shouldLoginWithExistingUser() throws Exception {
        String loginToken = "mocked-token";
        when(keycloakService.login("leonardosr@outlook.com", "leonardosr")).thenReturn(loginToken);

        LoginRequest loginRequest = new LoginRequest("leonardosr@outlook.com", "leonardosr");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(loginToken));
    }

    @Test
    void shouldFailToRegisterWhenKeycloakIsUnavailable() throws Exception {
        UsuarioRequest registerRequest = new UsuarioRequest(
            "Usuário de Teste",
            "test@example.com",
            "password123"
        );
        
        doThrow(new ResponseStatusException(UNAUTHORIZED, "Erro ao registrar usuário no Keycloak"))
            .when(keycloakService).register(any(UsuarioRequest.class));

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldFailToLoginWithInvalidCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest("invalid@example.com", "wrongpassword");
        
        doThrow(new ResponseStatusException(UNAUTHORIZED, "Credenciais inválidas"))
            .when(keycloakService).login(anyString(), anyString());

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldLogoutSuccessfully() throws Exception {
        String validToken = "valid-token";
        
        mockMvc.perform(delete("/auth/logout")
                .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk());
    }
} 