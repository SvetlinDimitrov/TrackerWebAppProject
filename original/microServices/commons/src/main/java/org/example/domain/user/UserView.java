package org.example.domain.user;

import java.math.BigDecimal;
import org.example.domain.user.enums.Gender;
import org.example.domain.user.enums.UserDetails;
import org.example.domain.user.enums.UserRole;
import org.example.domain.user.enums.WorkoutState;


public record UserView(

    String id,
    String username,
    String email,
    Double kilograms,
    Double height,
    WorkoutState workoutState,
    Gender gender,
    UserDetails userDetails,
    UserRole role,
    Integer age
) {

}
