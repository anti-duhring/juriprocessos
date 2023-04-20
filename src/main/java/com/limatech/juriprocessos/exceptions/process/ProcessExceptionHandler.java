package com.limatech.juriprocessos.exceptions.process;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProcessExceptionHandler {

    @ExceptionHandler({ProcessNotFoundException.class, ActivityNotFoundException.class, GroupNotFoundException.class})
    public ResponseEntity handle404Error(Exception exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}
