package com.jobscheduler.auth_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleNotFound(ResourceNotFound ex) {
        return Map.of(
                "status", 404,
                "message" , ex.getMessage(),
                "timestamp", LocalDateTime.now().toString()
        );
    }

    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleDuplicate(DuplicateResourceException ex) {
        return Map.of(
                "status", 409,
                "message" , ex.getMessage(),
                "timestamp", LocalDateTime.now().toString()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleGeneral(Exception ex) {
        return Map.of(
                "status", 500,
                "message", "Something went wrong",
                "timestamp", LocalDateTime.now().toString()
        );
    }
}
