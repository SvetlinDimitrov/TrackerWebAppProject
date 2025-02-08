package org.food.features.custom.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.food.features.custom.dto.NutrientRequestCreate;
import org.food.features.shared.enums.NutrientType.Mineral;

public class ValidMineralNutrientsValidator implements
    ConstraintValidator<ValidMineralNutrients, List<NutrientRequestCreate>> {

  private static final Set<String> MINERAL_NAMES =
      Set.of(Mineral.values()).stream()
          .map(Mineral::getName)
          .collect(Collectors.toSet());

  @Override
  public boolean isValid(List<NutrientRequestCreate> nutrients,
      ConstraintValidatorContext context) {
    if (nutrients == null || nutrients.isEmpty()) {
      return true;
    }
    for (NutrientRequestCreate nutrient : nutrients) {
      if (nutrient.name() == null || nutrient.name().isEmpty() ||
          !MINERAL_NAMES.contains(nutrient.name())) {
        String validNames = String.join(" / ", MINERAL_NAMES);
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                "Invalid mineral nutrient name. Valid names are: " + validNames)
            .addConstraintViolation();
        return false;
      }
    }
    return true;
  }
}