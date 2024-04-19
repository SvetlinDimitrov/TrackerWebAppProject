package org.trackerwebapp.meal_server.domain.dto;

import java.math.BigDecimal;
import org.trackerwebapp.meal_server.domain.entity.CalorieEntity;

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
}
