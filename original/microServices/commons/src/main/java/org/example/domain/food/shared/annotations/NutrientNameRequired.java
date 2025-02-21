package org.example.domain.food.shared.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NutrientNameRequiredValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NutrientNameRequired {

  String message() default "Nutrient name is required if min or max is specified";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}