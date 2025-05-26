package br.com.leonardosr.pagamentos.config;

import br.com.leonardosr.pagamentos.dto.PagamentoCadastroDto;
import br.com.leonardosr.pagamentos.model.Pagamento;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MappingConfig {

  @Bean
  public ModelMapper modelMapper() {
    ModelMapper mapper = new ModelMapper();

    // ignora mapeamento de 'id' ao converter PagamentoCadastroDto → Pagamento
    mapper.typeMap(PagamentoCadastroDto.class, Pagamento.class)
            .addMappings(m -> m.skip(Pagamento::setId));

    return mapper;
  }
}
