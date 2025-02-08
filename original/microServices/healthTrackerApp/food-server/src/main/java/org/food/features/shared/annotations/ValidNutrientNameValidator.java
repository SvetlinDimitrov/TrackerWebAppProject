package org.food.features.shared.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.food.features.shared.enums.NutrientType;

public class ValidNutrientNameValidator implements ConstraintValidator<ValidNutrientName, String> {

  private static Set<String> getValidNutrientNames() {
    Set<String> macroNames = Arrays.stream(NutrientType.Macronutrient.values())
        .map(NutrientType.Macronutrient::getName)
        .collect(Collectors.toSet());

    Set<String> mineralNames = Arrays.stream(NutrientType.Mineral.values())
        .map(NutrientType.Mineral::getName)
        .collect(Collectors.toSet());

    Set<String> vitaminNames = Arrays.stream(NutrientType.Vitamin.values())
        .map(NutrientType.Vitamin::getName)
        .collect(Collectors.toSet());

    return Stream.of(macroNames, mineralNames, vitaminNames)
        .flatMap(Set::stream)
        .collect(Collectors.toSet());
  }

  @Override
  public boolean isValid(String nutrientName, ConstraintValidatorContext context) {
    if (nutrientName == null || getValidNutrientNames().contains(nutrientName)) {
      return true;
    }

    String validNames = String.join(" / ", getValidNutrientNames());
    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate(
            "Invalid nutrient name. Valid names are: " + validNames)
        .addConstraintViolation();

    return false;
  }
}