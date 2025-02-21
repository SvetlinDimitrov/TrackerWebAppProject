package org.example.domain.food.shared.dto;

import java.math.BigDecimal;

public record NutrientView(
    String name,
    String unit,
    BigDecimal amount
) {
}