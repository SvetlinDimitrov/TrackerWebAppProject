package org.trackerwebapp.meal_server.domain.dto;

import java.math.BigDecimal;
import java.util.List;
import org.trackerwebapp.meal_server.domain.entity.MealEntity;

public record MealView(
    String id,
    String name,
    BigDecimal consumedCalories,
    List<FoodView> foods) {

  public static MealView toView(MealEntity entity , List<FoodView> foodView , BigDecimal caloriesConsumed) {
    return new MealView(
        entity.getId(),
        entity.getName(),
        caloriesConsumed,
        foodView);
  }
}
