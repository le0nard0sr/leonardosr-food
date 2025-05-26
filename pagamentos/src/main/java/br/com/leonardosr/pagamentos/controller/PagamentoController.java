package br.com.leonardosr.pagamentos.controller;

import br.com.leonardosr.pagamentos.dto.PagamentoAtualizarDto;
import br.com.leonardosr.pagamentos.dto.PagamentoCadastroDto;
import br.com.leonardosr.pagamentos.dto.PagamentoDto;
import br.com.leonardosr.pagamentos.service.PagamentoService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RequestMapping("/pagamentos")
@RestController
public class PagamentoController {

  private final PagamentoService service;
  
  @Autowired
  PagamentoController(PagamentoService service) {
    this.service = service;
  }

  @GetMapping("/{id}")
  public ResponseEntity<PagamentoDto> detalhar(@PathVariable Long id) {
    return ResponseEntity.ok(service.obterPorId(id));
  }

  @GetMapping
  public ResponseEntity<Page<PagamentoDto>> listar(@PageableDefault Pageable pageable) {
    Page<PagamentoDto> pagina = service.obterTodos(pageable);

    if (pagina.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(pagina);
  }

  @PostMapping
  public ResponseEntity<PagamentoDto> cadastrar(
          @RequestBody @Valid PagamentoCadastroDto dto, UriComponentsBuilder uriBuilder) {
    PagamentoDto pagamentoDto = service.cadastrar(dto);
    URI uri = uriBuilder.path("/pagamentos/{id}").buildAndExpand(pagamentoDto.getId()).toUri();
    return ResponseEntity.created(uri).body(pagamentoDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> remover(@PathVariable Long id) {
    service.remover(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<PagamentoDto> atualizar(
      @PathVariable Long id, @RequestBody @Valid PagamentoAtualizarDto dto) {
    return ResponseEntity.ok(service.atualizar(id, dto));
  }
}
