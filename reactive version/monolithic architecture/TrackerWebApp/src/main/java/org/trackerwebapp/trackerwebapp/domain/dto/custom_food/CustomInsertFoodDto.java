package org.trackerwebapp.trackerwebapp.domain.dto.custom_food;

import java.math.BigDecimal;
import java.util.List;

public record CustomInsertFoodDto(
    String name,
    CustomCalorieView calories,
    String measurement,
    BigDecimal size,
    List<CustomNutritionView> nutrients
) {

}
