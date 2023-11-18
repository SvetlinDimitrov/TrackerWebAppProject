package org.nutrition.exceptions;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class NutritionCreateException extends Exception {
    List<String> errors = new ArrayList<>();

    public NutritionCreateException(List<String> errors) {
        super();
        this.errors = errors;
    }
}
