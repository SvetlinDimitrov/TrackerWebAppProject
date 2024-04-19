package org.food.exception;

import lombok.Getter;

@Getter
public class InvalidUserTokenHeaderException  extends Exception{
    public InvalidUserTokenHeaderException(String message) {
        super(message);
    }
}
