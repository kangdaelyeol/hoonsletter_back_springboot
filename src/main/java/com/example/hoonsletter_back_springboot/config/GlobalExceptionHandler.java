package com.example.hoonsletter_back_springboot.config;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException e, WebRequest request) {
    return buildErrorResponse(e, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(EntityNotFoundException e, WebRequest request) {
    return buildErrorResponse(e, HttpStatus.BAD_REQUEST);
  }

  private ResponseEntity<Map<String, Object>> buildErrorResponse(Exception e, HttpStatus status) {
    Map<String, Object> body = new HashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", status.value());
    body.put("error", status.getReasonPhrase());
    body.put("message", e.getMessage());

    return new ResponseEntity<>(body, status);
  }

}
