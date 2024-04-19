package org.record.exceptions;

import lombok.Getter;

@Getter
public class InvalidJsonTokenException  extends  Exception{
    public InvalidJsonTokenException(String message) {
        super(message);
    }
}
