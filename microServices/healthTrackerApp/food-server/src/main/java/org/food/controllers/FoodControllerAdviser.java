package org.food.controllers;

import org.food.domain.dtos.SingleErrorResponse;
import org.food.exception.FoodException;
import org.food.exception.InvalidUserTokenHeaderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FoodControllerAdviser {
    @ExceptionHandler(FoodException.class)
    public ResponseEntity<SingleErrorResponse> handleFoodException(FoodException e) {
        return new ResponseEntity<>(new SingleErrorResponse(e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidUserTokenHeaderException.class)
    public ResponseEntity<SingleErrorResponse> handleInvalidUserTokenHeaderException(
            InvalidUserTokenHeaderException e) {
        return new ResponseEntity<>(new SingleErrorResponse(e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }
}
