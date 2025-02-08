package org.food.features.custom.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.food.features.custom.dto.NutrientRequestCreate;
import org.food.features.shared.entity.Nutrient;
import org.food.features.shared.enums.NutrientType.Macronutrient;

public class ValidMacroNutrientsValidator implements
    ConstraintValidator<ValidMacroNutrients, List<NutrientRequestCreate>> {

  private static final Set<String> MACRONUTRIENT_NAMES =
      Set.of(Macronutrient.values())
          .stream()
          .map(Macronutrient::getName)
          .collect(Collectors.toSet());

  @Override
  public boolean isValid(List<NutrientRequestCreate> nutrients, ConstraintValidatorContext context) {
    if (nutrients == null || nutrients.isEmpty()) {
      return true;
    }
    for (NutrientRequestCreate nutrient : nutrients) {
      if (nutrient.name() == null || nutrient.name().isEmpty() ||
          !MACRONUTRIENT_NAMES.contains(nutrient.name())) {
        String validNames = String.join(" / ", MACRONUTRIENT_NAMES);
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                "Invalid macronutrient name. Valid names are: " + validNames)
            .addConstraintViolation();
        return false;
      }
    }
    return true;
  }
}