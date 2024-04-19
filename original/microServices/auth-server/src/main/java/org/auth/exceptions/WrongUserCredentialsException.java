package org.auth.exceptions;

import lombok.Getter;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
public class WrongUserCredentialsException extends Exception{

    private final List<String> messageWithErrors = new ArrayList<>();

    public WrongUserCredentialsException(List<FieldError> errors) {
        super();

        errors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .forEach(error -> getMessageWithErrors().add(error));

    }

    public WrongUserCredentialsException() {
        messageWithErrors.add("Email or password is wrong");
    }
}
