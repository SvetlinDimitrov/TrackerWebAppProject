package org.auth.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import org.auth.model.enums.Gender;
import org.auth.model.enums.WorkoutState;
import org.hibernate.validator.constraints.Length;


public record UserEditRequest(

    @Length(min = 4)
    String username,

    @DecimalMin(value = "5")
    BigDecimal kilograms,

    WorkoutState workoutState,

    Gender gender,

    BigDecimal height,

    @Min(1)
    Integer age
) {
}
