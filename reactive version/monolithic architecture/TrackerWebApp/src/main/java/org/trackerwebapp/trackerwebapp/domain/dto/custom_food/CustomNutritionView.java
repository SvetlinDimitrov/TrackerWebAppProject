package org.trackerwebapp.trackerwebapp.domain.dto.custom_food;

import org.trackerwebapp.trackerwebapp.domain.entity.CustomNutritionEntity;

import java.math.BigDecimal;

public record CustomNutritionView(
    String name,
    String unit,
    BigDecimal amount
) {

  public static CustomNutritionView toView(CustomNutritionEntity entity) {
    return new CustomNutritionView(
        entity.getName(),
        entity.getUnit(),
        entity.getAmount()
    );
  }
}
