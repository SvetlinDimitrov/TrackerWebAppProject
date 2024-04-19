package org.trackerwebapp.customFood_server.domain.dto;

import java.math.BigDecimal;
import java.util.List;
import org.trackerwebapp.customFood_server.domain.entity.FoodEntity;

public record FoodView(
    String id,
    String name,
    CalorieView calories,
    String measurement,
    BigDecimal size,
    List<NutritionView> nutrients
) {

  public static FoodView toView(
      FoodEntity entity,
      CalorieView calorieView,
      List<NutritionView> nutrientViews
  ) {
    return new FoodView(
        entity.getId(),
        entity.getName(),
        calorieView,
        entity.getMeasurement(),
        entity.getSize(),
        nutrientViews
    );
  }
}
