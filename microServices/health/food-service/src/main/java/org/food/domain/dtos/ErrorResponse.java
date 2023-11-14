package org.food.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String errorMessage;
    private List<String> availableFoods;
}
