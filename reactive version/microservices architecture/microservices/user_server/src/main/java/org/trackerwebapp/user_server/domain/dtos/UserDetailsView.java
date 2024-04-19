package org.trackerwebapp.user_server.domain.dtos;

import java.math.BigDecimal;
import org.trackerwebapp.shared_interfaces.domain.enums.Gender;
import org.trackerwebapp.shared_interfaces.domain.enums.WorkoutState;
import org.trackerwebapp.user_server.domain.entity.UserDetails;

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
