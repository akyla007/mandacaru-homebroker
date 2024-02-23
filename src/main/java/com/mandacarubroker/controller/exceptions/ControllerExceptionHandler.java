
package com.mandacarubroker.controller.exceptions;

import com.mandacarubroker.service.exceptions.DataIntegratyViolationException;
import com.mandacarubroker.service.exceptions.StockNotFoundException;


import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(StockNotFoundException.class)
    public ResponseEntity<StandardError> handleObjectNotFoundException(StockNotFoundException ex) {
        StandardError standardError = new StandardError(LocalDateTime.now(), HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(standardError);
    }

    @ExceptionHandler(DataIntegratyViolationException.class)
    public ResponseEntity<StandardError>dataIntegrityViolationException(DataIntegratyViolationException dat) {
        StandardError standardError = new StandardError(LocalDateTime.now(), HttpStatus.CONFLICT, dat.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardError);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<StandardError> handleConstraintViolationException(ConstraintViolationException ex) {

        StandardError standardError = new StandardError(LocalDateTime.now(), HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardError);
    }
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<StandardError> validatorViolationException(ValidationException ex) {

        StandardError standardError = new StandardError(LocalDateTime.now(), HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardError);


    }
}
