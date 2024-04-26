package org.trackerwebapp.trackerwebapp.utils.record;

import org.trackerwebapp.trackerwebapp.domain.dto.BadRequestException;
import org.trackerwebapp.trackerwebapp.domain.dto.record.CustomNutritionView;
import org.trackerwebapp.trackerwebapp.domain.enums.AllowedNutrients;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomNutritionsValidator {

  public static Mono<List<CustomNutritionView>> validate(List<CustomNutritionView> nutritionViews) {

    if (nutritionViews == null || nutritionViews.isEmpty()) {
      return Mono.empty();
    }

    Set<String> allAvailableNames =
        Arrays.stream(AllowedNutrients.values()).map(AllowedNutrients::getNutrientName)
            .collect(Collectors.toSet());

    for (CustomNutritionView customNutritionView : nutritionViews) {
      if (!allAvailableNames.contains(customNutritionView.name())) {
        return Mono.error(new BadRequestException("Custom nutrition name: " + customNutritionView.name() + " is not available"));
      }
      if (customNutritionView.recommendedIntake().compareTo(BigDecimal.ZERO) < 0) {
        return Mono.error(new BadRequestException("Custom nutrition recommended intake: " + customNutritionView.recommendedIntake() +
            " for " + customNutritionView.name() +
            " cannot be less than 0"));

      }
    }
    return Mono.just(nutritionViews);
  }

}
