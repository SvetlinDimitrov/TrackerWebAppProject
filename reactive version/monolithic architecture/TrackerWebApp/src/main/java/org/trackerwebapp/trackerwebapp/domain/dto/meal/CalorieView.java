package org.trackerwebapp.trackerwebapp.domain.dto.meal;

import org.trackerwebapp.trackerwebapp.domain.entity.CalorieEntity;
import org.trackerwebapp.trackerwebapp.domain.entity.CustomCalorieEntity;
import org.trackerwebapp.trackerwebapp.domain.enums.AllowedCalorieUnits;

import java.math.BigDecimal;

public record CalorieView(

    BigDecimal amount,
    String unit
) {

  public static CalorieView toView(CalorieEntity entity) {
    return new CalorieView(
        entity.getAmount(),
        entity.getUnit()
    );
  }
  public static CalorieView toView(CustomCalorieEntity entity) {
    return new CalorieView(
        entity.getAmount(),
        AllowedCalorieUnits.CALORIE.name()
    );
  }
}
