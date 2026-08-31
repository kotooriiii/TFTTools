package com.tfttools.comps.exception;

import com.tfttools.comps.controller.CompController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Scoped to {@link CompController} only, mirroring {@code auth.exception.AuthExceptionHandler} -
 * error handling on every other controller is left untouched.
 */
@RestControllerAdvice(assignableTypes = CompController.class)
public class CompExceptionHandler
{
    @ExceptionHandler(CompLimitExceededException.class)
    public ResponseEntity<Map<String, String>> handleCompLimitExceeded(CompLimitExceededException e)
    {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(CompNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCompNotFound(CompNotFoundException e)
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException e)
    {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors())
        {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(fieldErrors);
    }
}
