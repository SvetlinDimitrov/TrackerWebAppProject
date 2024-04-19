package org.trackerwebapp.customFood_server.domain.dto;

import java.math.BigDecimal;
import org.trackerwebapp.customFood_server.domain.entity.CalorieEntity;

public record CalorieView(

    BigDecimal amount
) {

  public static CalorieView toView(CalorieEntity entity) {
    return new CalorieView(
        entity.getAmount()
    );
  }
}
