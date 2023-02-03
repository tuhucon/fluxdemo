package com.example.fluxdemo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(ISBNDuplicatedException.class)
    public final ResponseEntity<ErrorResponseBody> handleDefaultException(ISBNDuplicatedException e) {
        ErrorResponseBody errorResponseBody = new ErrorResponseBody("400009", "ISBN is duplicated");
        return createCustomResponse(HttpStatus.BAD_REQUEST, errorResponseBody);
    }

    private ResponseEntity<ErrorResponseBody> createCustomResponse(HttpStatus httpStatus, ErrorResponseBody errorResponseBody) {
        return new ResponseEntity<>(errorResponseBody, httpStatus);
    }
}
