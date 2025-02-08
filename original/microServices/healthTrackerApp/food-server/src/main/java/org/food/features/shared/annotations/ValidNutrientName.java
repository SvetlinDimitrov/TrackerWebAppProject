package org.food.features.shared.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidNutrientNameValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNutrientName {
    String message() default "Invalid nutrient name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}