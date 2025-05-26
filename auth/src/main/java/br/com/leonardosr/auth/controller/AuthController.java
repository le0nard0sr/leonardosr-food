package br.com.leonardosr.auth.controller;

import br.com.leonardosr.auth.dto.LoginRequest;
import br.com.leonardosr.auth.dto.TokenResponse;
import br.com.leonardosr.auth.dto.UsuarioRequest;
import br.com.leonardosr.auth.service.KeycloakService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários")
public class AuthController {

    private final KeycloakService keycloakService;

    @Operation(summary = "Login de usuário", description = "Realiza o login do usuário e retorna um token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class))),
        @ApiResponse(responseCode = "400", description = "Dados de login inválidos",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest request) {
        log.debug("Tentativa de login para o email: {}", request.getEmail());
        String token = keycloakService.login(request.getEmail(), request.getSenha());
        log.debug("Login realizado com sucesso para o email: {}", request.getEmail());
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @Operation(summary = "Registro de usuário", description = "Registra um novo usuário e retorna um token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class))),
        @ApiResponse(responseCode = "400", description = "Dados de registro inválidos",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "409", description = "Usuário já existe",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenResponse> register(@RequestBody @Valid UsuarioRequest request) {
        log.debug("Tentativa de registro para o email: {}", request.getEmail());
        String token = keycloakService.register(request);
        log.debug("Registro realizado com sucesso para o email: {}", request.getEmail());
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @PostMapping("/token")
    public ResponseEntity<?> getToken(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok(keycloakService.getToken(username, password));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
        return ResponseEntity.ok(keycloakService.refreshToken(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String refreshToken) {
        keycloakService.logout(refreshToken);
        return ResponseEntity.ok().build();
    }
} 