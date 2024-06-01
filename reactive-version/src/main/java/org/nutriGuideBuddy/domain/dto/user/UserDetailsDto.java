package org.nutriGuideBuddy.domain.dto.user;

import org.nutriGuideBuddy.domain.enums.WorkoutState;
import org.nutriGuideBuddy.domain.enums.Gender;

import java.math.BigDecimal;

public record UserDetailsDto(
    BigDecimal kilograms,
    BigDecimal height,
    Integer age,
    WorkoutState workoutState,
    Gender gender
) {

}
