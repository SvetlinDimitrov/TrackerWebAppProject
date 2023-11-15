package org.nutrition.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NutritionIntakeChangeDto {

    @NotBlank
    private String nutritionName;
    @NotNull
    private BigDecimal dailyConsumed;

}
