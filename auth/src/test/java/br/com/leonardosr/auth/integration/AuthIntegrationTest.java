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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private KeycloakService keycloakService;

    @Test
    void fullAuthenticationFlow() throws Exception {
        // Registro
        UsuarioRequest registerRequest = new UsuarioRequest(
            "Test User",
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
    void invalidRegistrationFlow() throws Exception {
        // Tentativa de registro com dados inválidos
        UsuarioRequest invalidRequest = new UsuarioRequest(
            "", // nome inválido
            "invalid-email", // email inválido
            "123" // senha muito curta
        );

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.title").value("Erro de validação"))
                .andExpect(jsonPath("$.detail").value("Erro de validação dos dados"))
                .andExpect(jsonPath("$.errors.nome").value("O nome é obrigatório"))
                .andExpect(jsonPath("$.errors.email").value("O email deve ser válido"))
                .andExpect(jsonPath("$.errors.senha").value("A senha deve ter no mínimo 6 caracteres"));
    }

    @Test
    void invalidLoginFlow() throws Exception {
        // Tentativa de login com credenciais inválidas
        LoginRequest invalidRequest = new LoginRequest(
            "invalid@email",
            "short"
        );

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.title").value("Erro de validação"))
                .andExpect(jsonPath("$.detail").value("Erro de validação dos dados"))
                .andExpect(jsonPath("$.errors.senha").value("A senha deve ter no mínimo 6 caracteres"));
    }
} 