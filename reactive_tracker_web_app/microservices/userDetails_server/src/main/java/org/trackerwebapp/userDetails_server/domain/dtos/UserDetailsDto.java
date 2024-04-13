package org.trackerwebapp.userDetails_server.domain.dtos;

import java.math.BigDecimal;
import org.trackerwebapp.shared_interfaces.domain.enums.Gender;
import org.trackerwebapp.shared_interfaces.domain.enums.WorkoutState;

public record UserDetailsDto(BigDecimal kilograms, BigDecimal height,
                             Integer age, WorkoutState workoutState, Gender gender) {

}
