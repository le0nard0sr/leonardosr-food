package br.com.leonardosr.pagamentos.dto;

import br.com.leonardosr.pagamentos.model.Status;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PagamentoDto {
  private Long id;
  private Status status;
  private Long pedidoId;
  private BigDecimal valor;
}
