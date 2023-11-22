package org.macronutrient.model.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {

    private String message;
    private List<String> availableElectrolyteNames;

}
