package org.food.features.custom.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidVitaminNutrientsValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidVitaminNutrients {
    String message() default "Invalid vitamin nutrient name.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}