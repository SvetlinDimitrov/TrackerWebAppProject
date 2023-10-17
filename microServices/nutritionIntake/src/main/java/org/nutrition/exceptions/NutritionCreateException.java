package org.nutrition.exceptions;

import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class NutritionCreateException extends Exception{
    List<String> errors = new ArrayList<>();

    public NutritionCreateException(List<FieldError> errors) {
        super();
        this.errors = errors.stream()
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .collect(Collectors.toList());
    }
}
