package org.food.features.shared.dto;

import java.math.BigDecimal;

public record NutrientView(
    String name,
    String unit,
    BigDecimal amount
) {
}