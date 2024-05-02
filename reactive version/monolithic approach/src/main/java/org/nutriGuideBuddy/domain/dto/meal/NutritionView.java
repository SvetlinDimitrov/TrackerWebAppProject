package org.nutriGuideBuddy.domain.dto.meal;

import org.nutriGuideBuddy.domain.entity.NutritionEntity;

import java.math.BigDecimal;

public record NutritionView(
    String name,
    String unit,
    BigDecimal amount
) {

  public static NutritionView toView(NutritionEntity entity) {
    return new NutritionView(
        entity.getName(),
        entity.getUnit(),
        entity.getAmount()
    );
  }
}
