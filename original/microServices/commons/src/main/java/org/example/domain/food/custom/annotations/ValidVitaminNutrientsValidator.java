package org.example.domain.food.custom.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.example.domain.food.custom.dto.NutrientRequestCreate;
import org.example.domain.food.enums.NutrientType.Vitamin;

public class ValidVitaminNutrientsValidator implements
    ConstraintValidator<ValidVitaminNutrients, List<NutrientRequestCreate>> {

  private static final Set<String> VITAMIN_NAMES =
      Set.of(Vitamin.values())
          .stream()
          .map(Vitamin::getName)
          .collect(Collectors.toSet());

  @Override
  public boolean isValid(List<NutrientRequestCreate> nutrients, ConstraintValidatorContext context) {
    if (nutrients == null || nutrients.isEmpty()) {
      return true;
    }
    for (NutrientRequestCreate nutrient : nutrients) {
      if (nutrient.name() == null || nutrient.name().isEmpty() ||
          !VITAMIN_NAMES.contains(nutrient.name())) {
        String validNames = String.join(" / ", VITAMIN_NAMES);
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                "Invalid vitamin nutrient name. Valid names are: " + validNames)
            .addConstraintViolation();
        return false;
      }
    }
    return true;
  }
}