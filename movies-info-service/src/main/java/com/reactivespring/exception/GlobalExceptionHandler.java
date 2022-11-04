package com.reactivespring.exception;

import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class) // Bean validations are captured and thrown as WebExchangeBindException
    public ResponseEntity<String> handleRequestBodyError(WebExchangeBindException ex) {
        log.error("Exception caught {} : {}", ex.getMessage(), ex);
        BindingResult bindingResult = ex.getBindingResult();
        String collect = bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage()).sorted()
                .collect(Collectors.joining(","));
        log.error("Exception messages:{}", collect);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(collect);
    }

}
