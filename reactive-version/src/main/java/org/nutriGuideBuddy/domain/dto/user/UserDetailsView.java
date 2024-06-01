package org.nutriGuideBuddy.domain.dto.user;

import org.nutriGuideBuddy.domain.entity.UserDetails;
import org.nutriGuideBuddy.domain.enums.Gender;
import org.nutriGuideBuddy.domain.enums.WorkoutState;

import java.math.BigDecimal;

public record UserDetailsView(
    BigDecimal kilograms,
    BigDecimal height,
    Integer age,
    WorkoutState workoutState,
    Gender gender
) {

  public static UserDetailsView toView(UserDetails entity) {
    return
        new UserDetailsView(
            entity.getKilograms(),
            entity.getHeight(),
            entity.getAge(),
            entity.getWorkoutState(),
            entity.getGender());
  }
}
