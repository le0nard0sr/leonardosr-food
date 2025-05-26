package br.com.leonardosr.pagamentos.dto;

import br.com.leonardosr.pagamentos.model.Status;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Usado na atualização (PUT/PATCH).
 * Todos os campos são opcionais — só serão aplicados se vierem não nulos.
 */
@Getter
@Setter
@NoArgsConstructor
public class PagamentoAtualizarDto {

    /**
     * Se informado, o status será atualizado.
     * Ex.: PENDENTE → PROCESSANDO → CONCLUIDO
     */
    private Status status;

    /**
     * Se informado, substitui o valor atual do pagamento.
     */
    @Positive(message = "O valor do pagamento deve ser positivo")
    private BigDecimal valor;

    /**
     * Se informado, atualiza o pedido associado.
     */
    @Positive(message = "O ID do pedido deve ser um número positivo")
    private Long pedidoId;
}