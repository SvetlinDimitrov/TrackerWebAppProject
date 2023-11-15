package org.storage.exception;

import lombok.Getter;

@Getter
public class FoodNameNotFoundException extends Exception{

    private final String errorMessage;

    public FoodNameNotFoundException(String message) {
        super(message);
        this.errorMessage = message;
    }
}
