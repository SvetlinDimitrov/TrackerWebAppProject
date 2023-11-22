package org.food;

import lombok.Getter;

import java.util.List;

@Getter
public class FoodNotFoundException extends Exception {

    private final ErrorResponse errorResponse;

    public FoodNotFoundException(String errorMessage, List<String> allFoods) {
        this.errorResponse = new ErrorResponse(errorMessage, allFoods);

    }
}
