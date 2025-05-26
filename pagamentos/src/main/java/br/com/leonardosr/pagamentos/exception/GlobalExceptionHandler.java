package br.com.leonardosr.pagamentos.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.net.URI;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(PagamentoNotFoundException.class)
  public ProblemDetail handlePagamentoNotFoundException(
      PagamentoNotFoundException exception, HttpServletRequest request) {
    ProblemDetail problema = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    problema.setType(ErrorType.NOT_FOUND.getUri());
    problema.setTitle("Pagamento não encontrado");
    problema.setDetail(exception.getMessage());
    problema.setInstance(URI.create(request.getRequestURI()));
    return problema;
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ProblemDetail handleDataIntegrityViolationException(
      DataIntegrityViolationException exception, HttpServletRequest request) {
    ProblemDetail problema = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problema.setType(ErrorType.DATA_INTEGRITY_VIOLATION.getUri());
    problema.setTitle("Violação de integridade de dados");
    problema.setDetail(exception.getMostSpecificCause().getMessage());
    problema.setInstance(URI.create(request.getRequestURI()));
    return problema;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception, HttpServletRequest request) {
    List<String> mensagens =
        exception.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .toList();
    ProblemDetail problema = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problema.setType(ErrorType.VALIDATION.getUri());
    problema.setTitle("Erro de validação");
    problema.setDetail("Ocorreram erros de validação");
    problema.setInstance(URI.create(request.getRequestURI()));
    problema.setProperty("errors", mensagens);
    return problema;
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ProblemDetail handleConstraintViolationException(
      ConstraintViolationException exception, HttpServletRequest request) {
    List<String> mensagens =
        exception.getConstraintViolations().stream()
            .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
            .toList();
    ProblemDetail problema = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problema.setType(ErrorType.CONSTRAINT_VIOLATION.getUri());
    problema.setTitle("Violação de restrição de dados");
    problema.setDetail("Ocorreram erros de validação de dados");
    problema.setInstance(URI.create(request.getRequestURI()));
    problema.setProperty("errors", mensagens);
    return problema;
  }

  @ExceptionHandler(Exception.class)
  public ProblemDetail handleException(Exception exception, HttpServletRequest request) {
    ProblemDetail problema = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    problema.setType(ErrorType.GENERIC.getUri());
    problema.setTitle("Erro interno do servidor");
    problema.setDetail(exception.getMessage());
    problema.setInstance(URI.create(request.getRequestURI()));
    return problema;
  }
}
