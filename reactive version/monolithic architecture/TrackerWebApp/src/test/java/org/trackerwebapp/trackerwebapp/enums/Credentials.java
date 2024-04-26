package org.trackerwebapp.trackerwebapp.enums;

import lombok.Getter;
import org.trackerwebapp.trackerwebapp.domain.enums.Gender;
import org.trackerwebapp.trackerwebapp.domain.enums.WorkoutState;

@Getter
public enum Credentials {
  VALID_USERNAME("John"),
  VALID_PASSWORD("12345"),
  VALID_EMAIL("test@abv.bg"),
  VALID_DETAIL_KILOGRAMS("81"),
  VALID_DETAIL_HEIGHT("162"),
  VALID_DETAIL_AGE("25"),
  VALID_DETAIL_WORKOUT(WorkoutState.LIGHTLY_ACTIVE.name()),
  VALID_DETAIL_GENDER(Gender.MALE.name()),
  VALID_MEAL_NAME("Split Bro"),
  VALID_FOOD_NAME("Salmon"),
  ;

  private final String value;

  Credentials(String value) {
    this.value = value;
  }
}
