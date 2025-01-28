package org.auth.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import org.auth.annotations.NotUsedEmailConstraint;
import org.auth.model.enums.Gender;
import org.auth.model.enums.WorkoutState;

public record UserCreateRequest(

    @Size(min = 4)
    @NotBlank
    @NotNull
    String username,

    @NotUsedEmailConstraint
    @NotBlank
    @Email
    String email,

    @NotBlank
    @Size(min = 5)
    String password,

    @DecimalMin(value = "5")
    BigDecimal kg,

    WorkoutState workoutState,

    Gender gender,

    BigDecimal height,

    @Min(1)
    Integer age
) {

}
