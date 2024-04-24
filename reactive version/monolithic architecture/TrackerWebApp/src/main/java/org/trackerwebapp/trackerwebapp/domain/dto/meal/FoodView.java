package org.trackerwebapp.trackerwebapp.domain.dto.meal;

import org.trackerwebapp.trackerwebapp.domain.entity.FoodEntity;

import java.math.BigDecimal;
import java.util.List;

public record FoodView(
    String id,
    String name,
    String measurement,
    BigDecimal size,
    CalorieView calorie,
    List<NutritionView> nutritionList) {

  public static FoodView toView(FoodEntity entity, List<NutritionView> nutritionList, CalorieView calorie) {
    return new FoodView(
        entity.getId(),
        entity.getName(),
        entity.getMeasurement(),
        entity.getSize(),
        calorie,
        nutritionList);
  }
}
