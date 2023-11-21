package org.record.exceptions;

import lombok.Getter;

@Getter
public class FoodException extends Exception {

    private final String errorMessage;

    public FoodException(String message) {
        super(message);
        this.errorMessage = message;
    }
}
