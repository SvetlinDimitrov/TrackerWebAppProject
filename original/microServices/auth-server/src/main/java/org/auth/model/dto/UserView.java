package org.auth.model.dto;

import java.math.BigDecimal;
import org.auth.model.enums.Gender;
import org.auth.model.enums.UserDetails;
import org.auth.model.enums.WorkoutState;

public record UserView(

    String id,
    String username,
    String email,
    BigDecimal kilograms,
    BigDecimal height,
    WorkoutState workoutState,
    Gender gender,
    UserDetails userDetails,
    Integer age
) {

}
