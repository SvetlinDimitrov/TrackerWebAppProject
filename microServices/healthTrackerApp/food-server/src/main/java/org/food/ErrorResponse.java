package org.food;

import java.util.List;

import lombok.Getter;

@Getter
public class ErrorResponse extends Exception{

    private final String errorMessage;
    private final List<String> foodsList;
   
    public ErrorResponse(String errorMessage , List<String> foodsList) {
        this.errorMessage = errorMessage;
        this.foodsList = foodsList;
    }

}
