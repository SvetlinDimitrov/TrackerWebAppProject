package org.nutriGuideBuddy.enums;

import lombok.Getter;
import org.nutriGuideBuddy.domain.enums.Gender;
import org.nutriGuideBuddy.domain.enums.WorkoutState;

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
  VALID_FOOD_MEASURE_UNIT("g"),
  VALID_FOOD_SERVING_UNIT("cup"),
  VALID_FOOD_SERVING_AMOUNT("350"),
  VALID_FOOD_SERVING_WEIGHT("100"),
  VALID_FOOD_INFO("something very inserting here"),
  VALID_FOOD_INFO_IMAGE("https://nix-tag-images.s3.amazonaws.com/1184_thumb.jpg"),
  VALID_FOOD_TO_SEARCH("chia seeds"),
  VALID_FOOD_BRANDED_ID("51c549ff97c3e6efadd60294"),
  ;

  private final String value;

  Credentials(String value) {
    this.value = value;
  }
}
