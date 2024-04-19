package org.trackerwebapp.shared_interfaces.domain.enums;

import lombok.Getter;

@Getter
public enum AllowedCalorieUnits {
  CALORIE("cal");
  private final String symbol;

  AllowedCalorieUnits(String symbol) {
    this.symbol = symbol;
  }

}
