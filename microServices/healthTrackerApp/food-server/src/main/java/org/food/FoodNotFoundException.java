package org.food;

import java.util.List;

import lombok.Getter;

@Getter
public class FoodNotFoundException extends Exception {
    
    private final List<String> foodsList;
    private final String errorMessage;

    public FoodNotFoundException(String message , List<String> foodsList) {
        super(message);
        this.foodsList = foodsList;
        this.errorMessage = message;
    }

}
