package org.auth.controllers;

import org.auth.exceptions.ExpiredTokenException;
import org.auth.exceptions.WrongUserCredentialsException;
import org.auth.model.dto.ErrorResponse;
import org.auth.model.dto.ErrorSingleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserControllerAdviser {
    @ExceptionHandler(WrongUserCredentialsException.class)
    public ResponseEntity<ErrorResponse> wrongCredentialsErrorCaught(WrongUserCredentialsException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessageWithErrors()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<ErrorSingleResponse> catchExpiredTokenException(ExpiredTokenException e) {
        return new ResponseEntity<>(new ErrorSingleResponse(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }
}
