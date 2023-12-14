package org.record.controllers;

import org.record.exceptions.InvalidJsonTokenException;
import org.record.exceptions.RecordCreationException;
import org.record.exceptions.RecordNotFoundException;
import org.record.exceptions.StorageException;
import org.record.exceptions.UserNotFoundException;
import org.record.model.dtos.ErrorResponse;
import org.record.model.dtos.ErrorSingleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RecordControllerAdviser {
    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ErrorSingleResponse> catchRecordNotFoundException(RecordNotFoundException e) {

        ErrorSingleResponse errorResponse = new ErrorSingleResponse();
        errorResponse.setErrorMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RecordCreationException.class)
    public ResponseEntity<ErrorResponse> catchRecordCreationException(RecordCreationException e) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getErrorMessages());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorSingleResponse> catchUserNotFoundException(UserNotFoundException e) {

        ErrorSingleResponse errorResponse = new ErrorSingleResponse();
        errorResponse.setErrorMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ErrorSingleResponse> catchStorageException(StorageException e) {

        ErrorSingleResponse errorResponse = new ErrorSingleResponse();
        errorResponse.setErrorMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidJsonTokenException.class)
    public ResponseEntity<ErrorSingleResponse> catchInvalidJsonTokenException(InvalidJsonTokenException e) {

        ErrorSingleResponse errorResponse = new ErrorSingleResponse();
        errorResponse.setErrorMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
