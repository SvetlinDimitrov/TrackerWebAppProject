package org.trackerwebapp.trackerwebapp.domain.dto.custom_food;

import org.trackerwebapp.trackerwebapp.domain.entity.CustomFoodEntity;

import java.math.BigDecimal;
import java.util.List;

public record CustomFoodView(
    String id,
    String name,
    CustomCalorieView calories,
    String measurement,
    BigDecimal size,
    List<CustomNutritionView> nutrients
) {

  public static CustomFoodView toView(
      CustomFoodEntity entity,
      CustomCalorieView calorieView,
      List<CustomNutritionView> nutrientViews
  ) {
    return new CustomFoodView(
        entity.getId(),
        entity.getName(),
        calorieView,
        entity.getMeasurement(),
        entity.getSize(),
        nutrientViews
    );
  }
}
