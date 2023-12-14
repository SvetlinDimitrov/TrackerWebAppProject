package org.storage.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.storage.exception.FoodException;
import org.storage.exception.InvalidJsonTokenException;
import org.storage.exception.StorageException;
import org.storage.model.errorResponses.ErrorResponse;

@ControllerAdvice
public class StorageControllerAdviser {
    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ErrorResponse> catchRecordNotFoundException(StorageException e) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FoodException.class)
    public ResponseEntity<ErrorResponse> catchRecordNotFoundException(FoodException e) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidJsonTokenException.class)
    public ResponseEntity<ErrorResponse> catchInvalidJsonTokenException(InvalidJsonTokenException e) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
