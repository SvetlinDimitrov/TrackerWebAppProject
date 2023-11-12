package org.storage.exception;

import org.storage.model.errorResponses.FoodErrorResponse;

import lombok.Getter;

@Getter
public class FoodNotFoundException extends Exception {

    private FoodErrorResponse errorResponse;

    public FoodNotFoundException(FoodErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

}
