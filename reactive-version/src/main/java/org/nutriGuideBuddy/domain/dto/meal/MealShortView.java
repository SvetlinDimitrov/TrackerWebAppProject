package org.nutriGuideBuddy.domain.dto.meal;

import org.nutriGuideBuddy.domain.entity.MealEntity;

import java.math.BigDecimal;
import java.util.List;

public record MealShortView(
    String id,
    String name,
    BigDecimal consumedCalories,
    List<ShortenFood> foods) {

  public static MealShortView toView(MealEntity entity, List<ShortenFood> foodView, BigDecimal caloriesConsumed) {
    return new MealShortView(
        entity.getId(),
        entity.getName(),
        caloriesConsumed,
        foodView);
  }
}
