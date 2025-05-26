package br.com.leonardosr.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Requisição de registro de novo usuário")
public class UsuarioRequest {
    
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome é obrigatório")
    @Schema(description = "Nome completo do usuário", example = "João da Silva")
    private String nome;
    
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser válido")
    @Schema(description = "Email do usuário", example = "joao.silva@email.com")
    private String email;
    
    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    @Schema(description = "Senha do usuário", example = "senha123", minLength = 6)
    private String senha;
} 