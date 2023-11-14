package org.nutrition.exceptions;

import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class IncorrectNutrientChangeException extends Exception{

    private List<String> errors;
    public IncorrectNutrientChangeException(List<FieldError> errors) {
        super();
        this.errors = errors.stream()
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .collect(Collectors.toList());
    }
    public IncorrectNutrientChangeException(String message) {
        super(message);
    }
}
