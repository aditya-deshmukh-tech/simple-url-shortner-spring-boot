package com.urlshortner.exceptions;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@RestControllerAdvice
@Order(1)
@CrossOrigin(origins = "*", allowedHeaders = {"authorization", "content-type", "accept", "x-requested-with"},
        methods = {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.POST, RequestMethod.PUT})
public class UrlShortExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UrlShortException.class)
    @CrossOrigin(origins = "*")
    public final ResponseEntity<ErrorResponse> handleUrlShortException(UrlShortException urlShortException) {
        ErrorResponse response = new ErrorResponse.ErrorResponseBuilder()
                .withErrorCode(urlShortException.getErrorCode())
                .withMessage(urlShortException.getErrorMessage())
                .withDetail(urlShortException.getDetails())
                .build();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getErrorCode()));
    }

    @ExceptionHandler(Exception.class)
    @CrossOrigin(origins = "*")
    public final ResponseEntity<ErrorResponse> handleException(Exception exception) {
        ErrorResponse response = new ErrorResponse.ErrorResponseBuilder()
                .withMessage(exception.getMessage())
                .withDetail(exception.getCause().getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.valueOf(500));
    }

    @ExceptionHandler(IOException.class)
    @CrossOrigin(origins = "*")
    public final ResponseEntity<ErrorResponse> handleIOException(IOException ioException) {
        ErrorResponse response = new ErrorResponse.ErrorResponseBuilder()
                .withMessage(ioException.getMessage())
                .withDetail(ioException.getCause().getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.valueOf(500));
    }

    @ExceptionHandler(InterruptedException.class)
    @CrossOrigin(origins = "*")
    public final ResponseEntity<ErrorResponse> handleInterruptedException(InterruptedException interruptedException) {
        ErrorResponse response = new ErrorResponse.ErrorResponseBuilder()
                .withMessage(interruptedException.getMessage())
                .withDetail(interruptedException.getCause().getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.valueOf(500));
    }
}
