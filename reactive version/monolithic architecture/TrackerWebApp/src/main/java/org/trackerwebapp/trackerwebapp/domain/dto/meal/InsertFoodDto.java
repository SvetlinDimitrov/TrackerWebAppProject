package org.trackerwebapp.trackerwebapp.domain.dto.meal;

import java.util.List;

public record InsertFoodDto(
    String name,
    CalorieView calories,
    List<ServingView> serving,
    List<NutritionView> nutrients
) {

}
