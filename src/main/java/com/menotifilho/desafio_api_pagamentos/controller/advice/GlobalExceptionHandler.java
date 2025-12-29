package com.menotifilho.desafio_api_pagamentos.controller.advice;

import com.menotifilho.desafio_api_pagamentos.exception.DataAlreadyExistsException;
import com.menotifilho.desafio_api_pagamentos.exception.InsufficientBalanceException;
import com.menotifilho.desafio_api_pagamentos.exception.TransferNotAllowedException;
import com.menotifilho.desafio_api_pagamentos.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ProblemDetails> handleUserNotFound(UserNotFoundException e, HttpServletRequest request){
        return buildResponse(HttpStatus.NOT_FOUND,"User Not Found", e.getMessage());
    }

    @ExceptionHandler({TransferNotAllowedException.class, InsufficientBalanceException.class, DataAlreadyExistsException.class})
    public ResponseEntity<ProblemDetails> handleBusinessRules(RuntimeException e, HttpServletRequest request){
        return buildResponse(HttpStatus.BAD_REQUEST, "Business Rule Violation", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetails> handleValidationErrors(MethodArgumentNotValidException e, HttpServletRequest request) {
        String errorMessages = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return buildResponse(HttpStatus.BAD_REQUEST, "Validation Error", errorMessages);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetails> handleGeneral(Exception e, HttpServletRequest request){
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error","An unexpected error occurred.");
    }

    private ResponseEntity<ProblemDetails> buildResponse(HttpStatus status, String title, String detail) {
        ProblemDetails problem = new ProblemDetails(
                status.value(),
                "API Error",
                title,
                detail,
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(problem);
    }


}
