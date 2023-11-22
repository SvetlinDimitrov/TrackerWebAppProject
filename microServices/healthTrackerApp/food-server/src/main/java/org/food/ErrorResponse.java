package org.food;

import java.util.List;

public class ErrorResponse extends Exception{

    private final String errorMessage;
    private final List<String> allFoods;

    public ErrorResponse(String errorMessage, List<String> allFoods) {
        this.errorMessage = errorMessage;
        this.allFoods = allFoods;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<String> getAllFoods() {
        return allFoods;
    }
}
