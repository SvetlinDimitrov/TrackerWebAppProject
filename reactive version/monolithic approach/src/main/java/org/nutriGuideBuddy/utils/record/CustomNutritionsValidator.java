package org.nutriGuideBuddy.utils.record;

import org.nutriGuideBuddy.domain.dto.BadRequestException;
import org.nutriGuideBuddy.domain.dto.record.NutritionView;
import org.nutriGuideBuddy.domain.enums.AllowedNutrients;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomNutritionsValidator {

  public static Mono<List<NutritionView>> validate(List<NutritionView> nutritionViews) {

    if (nutritionViews == null || nutritionViews.isEmpty()) {
      return Mono.empty();
    }

    Set<String> allAvailableNames =
        Arrays.stream(AllowedNutrients.values()).map(AllowedNutrients::getNutrientName)
            .collect(Collectors.toSet());

    for (NutritionView NutritionView : nutritionViews) {
      if (!allAvailableNames.contains(NutritionView.name())) {
        return Mono.error(new BadRequestException("Custom nutrition name: " + NutritionView.name() + " is not available"));
      }
      if (NutritionView.recommendedIntake().compareTo(BigDecimal.ZERO) < 0) {
        return Mono.error(new BadRequestException("Custom nutrition recommended intake: " + NutritionView.recommendedIntake() +
            " for " + NutritionView.name() +
            " cannot be less than 0"));

      }
    }
    return Mono.just(nutritionViews);
  }

}
