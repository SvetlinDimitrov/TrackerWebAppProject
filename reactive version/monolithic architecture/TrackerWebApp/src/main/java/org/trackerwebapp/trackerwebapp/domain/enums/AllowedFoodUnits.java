package org.trackerwebapp.trackerwebapp.domain.enums;

import lombok.Getter;

@Getter
public enum AllowedFoodUnits {

  GRAM("g");
  private final String symbol;

  AllowedFoodUnits(String symbol) {
    this.symbol = symbol;
  }
}
