package org.trackerwebapp.trackerwebapp.domain.dto.custom_food;

import org.trackerwebapp.trackerwebapp.domain.entity.CustomCalorieEntity;

import java.math.BigDecimal;

public record CustomCalorieView(

    BigDecimal amount
) {

  public static CustomCalorieView toView(CustomCalorieEntity entity) {
    return new CustomCalorieView(
        entity.getAmount()
    );
  }
}
