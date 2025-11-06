package com.sda.peaceverse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for centralized error handling.
 * Catches exceptions and returns consistent error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles runtime exceptions (e.g., API failures).
     * @param ex The exception.
     * @return Error response with details.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        ErrorResponse error = new ErrorResponse("Internal Server Error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles generic exceptions.
     * @param ex The exception.
     * @return Error response.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse("Unexpected Error", "An unexpected error occurred. Please try again.");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * DTO for error responses.
     */
    public record ErrorResponse(String error, String message) {}
}