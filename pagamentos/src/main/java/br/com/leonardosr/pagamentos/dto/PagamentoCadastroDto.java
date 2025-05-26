package br.com.leonardosr.pagamentos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Usado na criação (POST). O status e o id são definidos internamente; o cliente só informa
 * pedidoId e valor.
 */
@Getter
@Setter
@NoArgsConstructor
public class PagamentoCadastroDto {

  @NotNull(message = "O ID do pedido é obrigatório")
  @Positive(message = "O ID do pedido deve ser um número positivo")
  private Long pedidoId;

  @NotNull(message = "O valor do pagamento é obrigatório")
  @Positive(message = "O valor do pagamento deve ser positivo")
  private BigDecimal valor;
}
