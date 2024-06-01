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
            .map(measure -> new ServingView(
                Optional.ofNullable(measure.getQty())
                    .orElse(BigDecimal.ONE),
                Optional.ofNullable(measure.getServingWeight())
                    .orElse(BigDecimal.valueOf(100.0)),
                Optional.ofNullable(measure.getMeasure())
                    .orElse("g"))
            )
            .toList())
        .orElse(List.of());
  }

  public static ServingView getMainServing(FoodItem dto) {

    if (dto.getBrandName() == null) {
      return new ServingView(
          BigDecimal.valueOf(
              Optional.ofNullable(dto.getServingQty())
                  .orElse(1.0)
          ),
          BigDecimal.valueOf(
              Optional.ofNullable(dto.getServingWeightGrams())
                  .orElse(100.0)
          ),
          Optional.ofNullable(dto.getServingUnit())
              .orElse("g"));
    }
    return new ServingView(
        BigDecimal.valueOf(
            Optional.ofNullable(dto.getServingQty())
                .orElse(1.0)
        ),
        Optional.ofNullable(dto.getServingWeightGrams())
            .map(BigDecimal::valueOf)
            .orElse(
                BigDecimal.valueOf(
                    Optional.ofNullable(dto.getNfMetricQty())
                        .orElse(100.0)
                )
            ),
        Optional.ofNullable(dto.getServingUnit())
            .orElse("g"));
  }
}
