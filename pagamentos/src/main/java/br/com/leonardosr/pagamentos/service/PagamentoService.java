package br.com.leonardosr.pagamentos.service;

import br.com.leonardosr.pagamentos.dto.PagamentoAtualizarDto;
import br.com.leonardosr.pagamentos.dto.PagamentoCadastroDto;
import br.com.leonardosr.pagamentos.dto.PagamentoDto;
import br.com.leonardosr.pagamentos.exception.PagamentoNotFoundException;
import br.com.leonardosr.pagamentos.model.Pagamento;
import br.com.leonardosr.pagamentos.model.Status;
import br.com.leonardosr.pagamentos.repository.PagamentoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PagamentoService {

  private final PagamentoRepository repository;
  private final ModelMapper mapper;

  @Autowired
  public PagamentoService(PagamentoRepository repository, ModelMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  public PagamentoDto cadastrar(PagamentoCadastroDto dto) {
    Pagamento pagamento = toEntity(dto);
    pagamento.setStatus(Status.PENDENTE);
    Pagamento salvo = repository.save(pagamento);
    return toDto(salvo);
  }

  public PagamentoDto obterPorId(Long id) {
    Pagamento pagamento = repository.findById(id)
            .orElseThrow(() -> new PagamentoNotFoundException(id));
    return toDto(pagamento);
  }

  public Page<PagamentoDto> obterTodos(Pageable pageable) {
    return repository.findAll(pageable)
            .map(this::toDto);
  }

  public void remover(Long id) {
    Pagamento pagamento = repository.findById(id)
            .orElseThrow(() -> new PagamentoNotFoundException(id));
    repository.delete(pagamento);
  }

  public PagamentoDto atualizar(Long id, PagamentoAtualizarDto dto) {
    Pagamento pagamento = repository.findById(id)
            .orElseThrow(() -> new PagamentoNotFoundException(id));

    applyUpdates(dto, pagamento);

    Pagamento atualizado = repository.save(pagamento);
    return toDto(atualizado);
  }

  private PagamentoDto toDto(Pagamento pagamento) {
    return mapper.map(pagamento, PagamentoDto.class);
  }

  private Pagamento toEntity(PagamentoCadastroDto dto) {
    return mapper.map(dto, Pagamento.class);
  }

  private void applyUpdates(PagamentoAtualizarDto dto, Pagamento pagamento) {
    if (dto.getStatus() != null) {
      pagamento.setStatus(dto.getStatus());
    }
    if (dto.getValor() != null) {
      pagamento.setValor(dto.getValor());
    }
    if (dto.getPedidoId() != null) {
      pagamento.setPedidoId(dto.getPedidoId());
    }
  }
}