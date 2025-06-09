package com.hiricus.dcs.exception.advice;

import com.hiricus.dcs.exception.RoleViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RoleViolationHandler {

    @ExceptionHandler(RoleViolationException.class)
    public ResponseEntity<String> handle(RoleViolationException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }
}
