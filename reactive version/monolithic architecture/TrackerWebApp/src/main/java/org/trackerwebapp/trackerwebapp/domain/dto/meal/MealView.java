package org.trackerwebapp.trackerwebapp.domain.dto.meal;

import org.trackerwebapp.trackerwebapp.domain.entity.MealEntity;

import java.math.BigDecimal;
import java.util.List;

public record MealView(
    String id,
    String name,
    BigDecimal consumedCalories,
    List<FoodView> foods) {

  public static MealView toView(MealEntity entity, List<FoodView> foodView, BigDecimal caloriesConsumed) {
    return new MealView(
        entity.getId(),
        entity.getName(),
        caloriesConsumed,
        foodView);
  }
}
