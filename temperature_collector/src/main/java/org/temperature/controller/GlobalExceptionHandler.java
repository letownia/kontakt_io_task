package org.temperature.controller;

import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> defaultExceptionHandler(Exception exception) {
    log.error("Unhandled exception : " + exception.getMessage(), exception);
    return new ResponseEntity<>("Internal server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
  }
  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<String> addExceptionMessageToResponseEntity(ResponseStatusException exception) {
    log.info("ResponseStatusException : " + exception.getMessage() + " statusCode :" +  exception.getStatusCode());
    return new ResponseEntity<>(exception.getMessage(), exception.getStatusCode());
  }
}