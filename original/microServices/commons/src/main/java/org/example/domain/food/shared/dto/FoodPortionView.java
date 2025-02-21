package org.example.domain.food.shared.dto;

import java.math.BigDecimal;

public record FoodPortionView(
    String name,
    String modifier,
    BigDecimal gramWeight,
    BigDecimal amount
) {
}