package org.nutriGuideBuddy.domain.enums;

import lombok.Getter;

@Getter
public enum AllowedCalorieUnits {
  CALORIE("kcal");
  private final String symbol;

  AllowedCalorieUnits(String symbol) {
    this.symbol = symbol;
  }

}
