package org.example.domain.food.shared.dto;

import java.math.BigDecimal;

public record CaloriesView(
    String name,
    BigDecimal amount,
    String unit
) {

  public CaloriesView(BigDecimal amount) {
    this("Energy", amount, "kcal");
  }
}