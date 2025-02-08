package org.food.features.embedded.dto;

import jakarta.validation.constraints.DecimalMin;
import org.food.features.shared.annotations.NutrientNameRequired;
import org.food.features.shared.annotations.ValidNutrientName;

@NutrientNameRequired
public record EmbeddedFilterCriteria(
    String description,

    @ValidNutrientName
    String nutrientName,

    @DecimalMin(value = "0.0", message = "Min value must be greater than 0")
    Double min,

    @DecimalMin(value = "0.0", message = "Max value must be greater than 0")
    Double max
) {

}