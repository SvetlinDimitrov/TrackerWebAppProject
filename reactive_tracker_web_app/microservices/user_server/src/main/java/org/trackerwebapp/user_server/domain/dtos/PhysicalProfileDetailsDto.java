package org.trackerwebapp.user_server.domain.dtos;

import java.math.BigDecimal;
import org.trackerwebapp.user_server.domain.enums.Gender;
import org.trackerwebapp.user_server.domain.enums.WorkoutState;

public record PhysicalProfileDetailsDto(BigDecimal kilograms, BigDecimal height,
                                        Integer age, WorkoutState workoutState, Gender gender) {

}
