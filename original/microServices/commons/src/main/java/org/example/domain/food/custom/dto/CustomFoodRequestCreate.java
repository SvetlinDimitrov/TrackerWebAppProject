package org.example.domain.food.custom.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import org.example.domain.food.custom.annotations.ValidMacroNutrients;
import org.example.domain.food.custom.annotations.ValidMineralNutrients;
import org.example.domain.food.custom.annotations.ValidVitaminNutrients;


public record CustomFoodRequestCreate(
    @NotBlank(message = "Food name is required.")
    String description,

    @NotNull(message = "Calories are required.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Invalid calories.")
    BigDecimal calories,

    @NotNull(message = "Size is required.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Invalid size.")
    BigDecimal size,

    @Valid
    @ValidVitaminNutrients
    List<NutrientRequestCreate> vitaminNutrients,

    @Valid
    @ValidMacroNutrients
    List<NutrientRequestCreate> macroNutrients,

    @Valid
    @ValidMineralNutrients
    List<NutrientRequestCreate> mineralNutrients
) {

}