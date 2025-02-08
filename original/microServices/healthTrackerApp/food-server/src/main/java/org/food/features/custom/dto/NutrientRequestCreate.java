package org.food.features.custom.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record NutrientRequestCreate(
    @NotBlank(message = "Nutrient name is required.")
    String name,

    @NotBlank(message = "Nutrient unit is required.")
    String unit,

    @NotNull(message = "Nutrient amount is required.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Nutrient amount must be greater than zero.")
    BigDecimal amount
) {

}