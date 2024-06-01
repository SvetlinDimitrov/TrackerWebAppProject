package org.nutriGuideBuddy.domain.dto.meal;

import java.util.List;

public record InsertFoodDto(
    String name,
    CalorieView calories,
    ServingView mainServing,
    FoodInfoView foodDetails,
    List<ServingView> otherServing,
    List<NutritionView> nutrients
) {

}
