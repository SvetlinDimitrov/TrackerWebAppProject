package org.trackerwebapp.trackerwebapp.domain.enums;

import lombok.Getter;

@Getter
public enum AllowedCalorieUnits {
  CALORIE("cal");
  private final String symbol;

  AllowedCalorieUnits(String symbol) {
    this.symbol = symbol;
  }

}
