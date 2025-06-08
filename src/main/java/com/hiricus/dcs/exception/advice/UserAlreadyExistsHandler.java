package com.hiricus.dcs.exception.advice;

import com.hiricus.dcs.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserAlreadyExistsHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handle(UserAlreadyExistsException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }
}
