package br.com.leonardosr.pagamentos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pagamentos")
public class Pagamento {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "O status do pagamento é obrigatório")
  @Enumerated(EnumType.STRING)
  private Status status;

  @NotNull(message = "O ID do pedido é obrigatório")
  private Long pedidoId;

  @NotNull(message = "O valor do pagamento é obrigatório")
  @Positive(message = "O valor do pagamento deve ser positivo")
  private BigDecimal valor;
}
