package org.food.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
public class InvalidUserTokenHeaderException  extends Exception{
    public InvalidUserTokenHeaderException(String message) {
        super(message);
    }
}
