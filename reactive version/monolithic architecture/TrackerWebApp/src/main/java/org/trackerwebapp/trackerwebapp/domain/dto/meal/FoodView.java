package org.trackerwebapp.trackerwebapp.domain.dto.meal;

import org.trackerwebapp.trackerwebapp.domain.entity.CustomFoodEntity;
import org.trackerwebapp.trackerwebapp.domain.entity.FoodEntity;

import java.math.BigDecimal;
import java.util.List;

public record FoodView(
    String id,
    String name,
    List<ServingView> serving,
    CalorieView calorie,
    List<NutritionView> nutritionList) {

  public static FoodView toView(FoodEntity entity, List<NutritionView> nutritionList, CalorieView calorie , List<ServingView> servings) {
    return new FoodView(
        entity.getId(),
        entity.getName(),
        servings,
        calorie,
        nutritionList);
  }
  public static FoodView toView(CustomFoodEntity entity, List<NutritionView> nutritionList, CalorieView calorie , List<ServingView> servings) {
    return new FoodView(
        entity.getId(),
        entity.getName(),
        servings,
        calorie,
        nutritionList);
  }
}
