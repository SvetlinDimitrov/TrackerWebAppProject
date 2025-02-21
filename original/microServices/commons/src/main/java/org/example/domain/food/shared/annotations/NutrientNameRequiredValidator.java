package org.example.domain.food.shared.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.domain.food.embedded.dto.EmbeddedFilterCriteria;

public class NutrientNameRequiredValidator implements
    ConstraintValidator<NutrientNameRequired, EmbeddedFilterCriteria> {

  @Override
  public boolean isValid(EmbeddedFilterCriteria filterCriteria, ConstraintValidatorContext context) {
    if ((filterCriteria.min() != null || filterCriteria.max() != null) && (
        filterCriteria.nutrientName() == null || filterCriteria.nutrientName().isEmpty())) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(
              "Nutrient name is required if min or max is specified")
          .addPropertyNode("nutrientName")
          .addConstraintViolation();
      return false;
    }
    return true;
  }
}