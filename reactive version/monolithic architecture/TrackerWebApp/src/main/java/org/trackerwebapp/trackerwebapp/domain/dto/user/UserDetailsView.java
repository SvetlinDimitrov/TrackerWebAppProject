package org.trackerwebapp.trackerwebapp.domain.dto.user;

import org.trackerwebapp.trackerwebapp.domain.entity.UserDetails;
import org.trackerwebapp.trackerwebapp.domain.enums.Gender;
import org.trackerwebapp.trackerwebapp.domain.enums.WorkoutState;

import java.math.BigDecimal;

public record UserDetailsView(
    String id,
    BigDecimal kilograms,
    BigDecimal height,
    Integer age,
    WorkoutState workoutState,
    Gender gender,
    String userId
) {

  public static UserDetailsView toView(UserDetails entity) {
    return
        new UserDetailsView(
            entity.getId(),
            entity.getKilograms(),
            entity.getHeight(),
            entity.getAge(),
            entity.getWorkoutState(),
            entity.getGender(),
            entity.getUserId());
  }
}