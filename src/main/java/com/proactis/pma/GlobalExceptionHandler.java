package com.proactis.pma;

import com.proactis.pma.dto.ValidationError;
import com.proactis.pma.exception.ProductNotFoundException;
import com.proactis.pma.exception.StoreHasProductsException;
import com.proactis.pma.exception.StoreNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ValidationError> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().body(
                ValidationError.message("An error occurred due to data constraint violation")
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(
                ValidationError.errors(errors)
        );
    }

    @ExceptionHandler(exception = {ProductNotFoundException.class, StoreNotFoundException.class, StoreHasProductsException.class})
    public ResponseEntity<ValidationError> handleCustomException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(
                ValidationError.message(ex.getMessage())
        );
    }
}
