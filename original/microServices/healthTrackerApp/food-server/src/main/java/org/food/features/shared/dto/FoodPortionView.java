package org.food.features.shared.dto;

import java.math.BigDecimal;

public record FoodPortionView(
    String name,
    String modifier,
    BigDecimal gramWeight,
    BigDecimal amount
) {
}