package org.auth.exceptions;

import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class WrongUserCredentialsException extends Exception{

    private final String messageWithErrors;

    public WrongUserCredentialsException(List<FieldError> errors) {
        super();

        this.messageWithErrors = errors.stream()
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .collect(Collectors.joining("\n"));

    }
}
