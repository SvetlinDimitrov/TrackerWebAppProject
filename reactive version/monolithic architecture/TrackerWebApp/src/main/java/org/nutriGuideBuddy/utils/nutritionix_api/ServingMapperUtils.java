package org.nutriGuideBuddy.utils.nutritionix_api;

import org.nutriGuideBuddy.domain.dto.meal.ServingView;
import org.nutriGuideBuddy.domain.dto.nutritionxApi.FoodItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ServingMapperUtils {

  public static List<ServingView> getServings(FoodItem dto) {

    return Optional.ofNullable(dto.getMeasures())
        .map(list -> list
            .stream()
            .map(measure -> new ServingView(measure.getQty(), measure.getServingWeight(), measure.getMeasure()))
            .toList())
        .orElse(List.of());
  }

  public static ServingView getMainServing(FoodItem dto) {

    if (dto.getBrandName() == null) {
      return new ServingView(BigDecimal.valueOf(dto.getServingQty()), BigDecimal.valueOf(dto.getServingWeightGrams()), dto.getServingUnit());
    }
    return new ServingView(BigDecimal.valueOf(
        dto.getServingQty()),
        Optional.ofNullable(dto.getServingWeightGrams())
            .map(BigDecimal::valueOf)
            .orElse(BigDecimal.valueOf(dto.getNfMetricQty())),
        dto.getServingUnit());
  }
}
