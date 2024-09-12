package com.api.v1.tarefas.exceptions;

import com.api.v1.tarefas.utils.ResponsePadraoDTO;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@ControllerAdvice
public class GlobalExceptionHandle{

    @ExceptionHandler(ApiSuccessException.class)
    public ResponseEntity<ResponsePadraoDTO> ApiSuccessException(ApiSuccessException e, HttpServletRequest httpServletRequest) {
        ResponsePadraoDTO success = ResponsePadraoDTO.sucesso(e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(success);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ResponsePadraoDTO> ApiException(ApiException e, HttpServletRequest httpServletRequest) {
        ResponsePadraoDTO erro = ResponsePadraoDTO.falha(e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(erro);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponsePadraoDTO> handleInvalidDataType(HttpMessageNotReadableException ex) {
        ResponsePadraoDTO erro = new ResponsePadraoDTO("Erro de Tipo de Dado", "Dados fornecidos são inválidos ou estão em um formato incorreto.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponsePadraoDTO> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String errorMessage = String.format("O parâmetro '%s' recebeu o valor '%s', que é de um tipo inválido. O tipo esperado é '%s'.",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

        ResponsePadraoDTO erro = new ResponsePadraoDTO("Erro de Tipo de Argumento", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorMessage> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        String supportedMethods = String.join(", ", ex.getSupportedHttpMethods().toString());

        List<Map<String, String>> errors = new ArrayList<>();

        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("method", ex.getMethod());
        errorDetails.put("message", "Método HTTP não suportado");
        errorDetails.put("supportedMethods", supportedMethods);

        errors.add(errorDetails);

        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                Instant.now(),
                "Método HTTP Inválido",
                errors
        );

        return new ResponseEntity<>(errorMessage, HttpStatus.METHOD_NOT_ALLOWED);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<Map<String, String>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> {
                    Map<String, String> errorDetails = new HashMap<>();
                    errorDetails.put("field", error.getField());
                    errorDetails.put("message", error.getDefaultMessage());
                    return errorDetails;
                })
                .collect(Collectors.toList());

        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                Instant.now(),
                "Erro de Validação",
                errors
        );

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }




}
