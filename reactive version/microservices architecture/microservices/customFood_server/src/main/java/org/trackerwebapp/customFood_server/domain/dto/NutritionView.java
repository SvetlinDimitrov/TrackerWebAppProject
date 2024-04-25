package org.trackerwebapp.customFood_server.domain.dto;

import java.math.BigDecimal;
import org.trackerwebapp.customFood_server.domain.entity.NutritionEntity;

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