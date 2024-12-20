package com.example.shop.clothesshop.exceptions.handler;


import com.example.shop.clothesshop.dtos.base.ErrorResponse;
import com.example.shop.clothesshop.exceptions.DbNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DbNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePositionNotFoundException(DbNotFoundException ex) {
        log.error("DbRowNotFoundException exception: ", ex);
        ErrorResponse errorResponse = new ErrorResponse(ex.getError(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException exception: ", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(IllegalArgumentException ex) {
        log.error("IllegalArgumentException exception: ", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(ConstraintViolationException ex) {
        log.error("ConstraintViolationException exception: ", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getConstraintViolations().stream().findFirst().get().getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(UsernameNotFoundException ex) {
        log.error("UsernameNotFoundException exception: ", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    private static String getStackTrace(Throwable ex) {
        filterStackTracesByProjectPackage(ex);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);

        return sw.toString();
    }

    private static void filterStackTracesByProjectPackage(Throwable ex) {
        if (ex == null) return;

        StackTraceElement[] stackTraces = Arrays.stream(ex.getStackTrace())
                .filter(se -> se.getClassName().startsWith("com."))
                .toArray(StackTraceElement[]::new);

        ex.setStackTrace(stackTraces);
    }
}
