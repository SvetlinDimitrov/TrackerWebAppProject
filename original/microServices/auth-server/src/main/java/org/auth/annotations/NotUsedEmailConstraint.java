package org.auth.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy  = NotUsedEmailValidator.class)
@Target({ElementType.METHOD , ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotUsedEmailConstraint {
    String message() default "email already exits";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
