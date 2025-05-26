package br.com.leonardosr.auth.controller;

import br.com.leonardosr.auth.config.TestSecurityConfig;
import br.com.leonardosr.auth.dto.LoginRequest;
import br.com.leonardosr.auth.dto.TokenResponse;
import br.com.leonardosr.auth.dto.UsuarioRequest;
import br.com.leonardosr.auth.service.KeycloakService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

@WebMvcTest(AuthController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private KeycloakService keycloakService;

    @Test
    void loginSuccess() throws Exception {
        LoginRequest request = new LoginRequest("user@test.com", "password123");
        String token = "jwt-token";

        when(keycloakService.login(anyString(), anyString())).thenReturn(token);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token));
    }

    @Test
    void loginInvalidEmail() throws Exception {
        LoginRequest request = new LoginRequest("invalid-email", "password123");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.title").value("Erro de validação"))
                .andExpect(jsonPath("$.detail").value("Erro de validação dos dados"))
                .andExpect(jsonPath("$.errors.email").value("O email deve ser válido"));
    }

    @Test
    void registerSuccess() throws Exception {
        UsuarioRequest request = new UsuarioRequest("Test User", "user@test.com", "password123");
        String token = "jwt-token";

        when(keycloakService.register(any(UsuarioRequest.class))).thenReturn(token);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token));
    }

    @Test
    void registerInvalidData() throws Exception {
        UsuarioRequest request = new UsuarioRequest("", "", "123"); // dados inválidos

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.title").value("Erro de validação"))
                .andExpect(jsonPath("$.detail").value("Erro de validação dos dados"))
                .andExpect(jsonPath("$.errors.nome").value("O nome é obrigatório"))
                .andExpect(jsonPath("$.errors.email").value("O email é obrigatório"))
                .andExpect(jsonPath("$.errors.senha").value("A senha deve ter no mínimo 6 caracteres"));
    }
} 