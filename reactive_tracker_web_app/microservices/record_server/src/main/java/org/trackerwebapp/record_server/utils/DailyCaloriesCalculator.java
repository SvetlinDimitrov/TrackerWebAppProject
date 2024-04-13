package org.trackerwebapp.record_server.utils;

import java.math.BigDecimal;
import org.trackerwebapp.record_server.domain.entity.UserDetailsEntity;

public class DailyCaloriesCalculator {

  public static BigDecimal getCaloriesPerDay(UserDetailsEntity userDetails) {
    BigDecimal BMR = BMRCalc.calculateBMR(userDetails);
    return switch (userDetails.getWorkoutState()) {
      case SEDENTARY -> BMR.multiply(new BigDecimal("1.2"));
      case LIGHTLY_ACTIVE -> BMR.multiply(new BigDecimal("1.375"));
      case MODERATELY_ACTIVE -> BMR.multiply(new BigDecimal("1.55"));
      case VERY_ACTIVE -> BMR.multiply(new BigDecimal("1.725"));
      case SUPER_ACTIVE -> BMR.multiply(new BigDecimal("1.9"));
    };
  }
}
