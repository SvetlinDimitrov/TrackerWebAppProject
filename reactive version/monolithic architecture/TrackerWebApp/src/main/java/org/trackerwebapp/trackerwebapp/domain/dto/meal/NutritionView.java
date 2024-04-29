package org.trackerwebapp.trackerwebapp.domain.dto.meal;

import org.trackerwebapp.trackerwebapp.domain.entity.CustomNutritionEntity;
import org.trackerwebapp.trackerwebapp.domain.entity.NutritionEntity;

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

  public static NutritionView toView(CustomNutritionEntity entity) {
    return new NutritionView(
        entity.getName(),
        entity.getUnit(),
        entity.getAmount()
    );
  }
}
