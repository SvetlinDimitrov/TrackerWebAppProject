package org.trackerwebapp.user_server.domain.dtos;

import java.math.BigDecimal;
import org.trackerwebapp.user_server.domain.entity.PhysicalProfileDetails;
import org.trackerwebapp.user_server.domain.enums.Gender;
import org.trackerwebapp.user_server.domain.enums.WorkoutState;

public record PhysicalProfileDetailsView(String id, BigDecimal kilograms, BigDecimal height,
                                         Integer age, WorkoutState workoutState, Gender gender) {

  public static PhysicalProfileDetailsView fromEntity(PhysicalProfileDetails entity) {
    return new PhysicalProfileDetailsView(entity.getId().toString(), entity.getKilograms(),
        entity.getHeight(), entity.getAge(), entity.getWorkoutState(), entity.getGender());
  }
}
