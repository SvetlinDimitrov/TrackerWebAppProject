package org.trackerwebapp.trackerwebapp.domain.dto.user;

import org.trackerwebapp.trackerwebapp.domain.enums.Gender;
import org.trackerwebapp.trackerwebapp.domain.enums.WorkoutState;

import java.math.BigDecimal;

public record UserDetailsDto(
    BigDecimal kilograms,
    BigDecimal height,
    Integer age,
    WorkoutState workoutState,
    Gender gender
) {

}
