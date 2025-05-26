package br.com.leonardosr.pagamentos.exception;

import java.net.URI;

/** Enum que centraliza os tipos de erro e as URIs correspondentes conforme RFC7807. */
public enum ErrorType {
  NOT_FOUND("https://www.leonardosr.com.br/errors/pagamento-not-found"),
  VALIDATION("https://www.leonardosr.com.br/errors/validation-error"),
  DATA_INTEGRITY_VIOLATION("https://www.leonardosr.com.br/errors/data-integrity-violation"),
  CONSTRAINT_VIOLATION("https://www.leonardosr.com.br/errors/constraint-violation"),
  GENERIC("https://www.leonardosr.com.br/errors/internal-server-error");

  private final String uri;

  ErrorType(String uri) {
    this.uri = uri;
  }

  public URI getUri() {
    return URI.create(this.uri);
  }
}
