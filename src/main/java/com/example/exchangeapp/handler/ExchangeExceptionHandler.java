package com.example.exchangeapp.handler;

import com.example.exchangeapp.exceptions.ExchangeException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;

@ControllerAdvice
public class ExchangeExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String DESCRIPTION = "description";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        Map<String, String> errors = new HashMap<>();

        final String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .map(error -> ((FieldError) error).getField() + " " + error.getDefaultMessage() + ". ")
                .collect(joining(""));

        errors.put(DESCRIPTION, errorMessage);

        return handleExceptionInternal(ex, errors, headers, status, request);
    }

    @ExceptionHandler(ExchangeException.class)
    public ResponseEntity<Object> handlePrivatBankApiUnavailable(ExchangeException ex, WebRequest request){
        Map<String, String> errors = new HashMap<>();

        errors.put(DESCRIPTION, ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_GATEWAY);
    }
}
