package org.record.features.record.utils;

import java.math.BigDecimal;
import java.util.Random;
import org.example.domain.user.dto.UserView;
import org.example.domain.user.enums.Gender;

public class RecordUtils {

  public static BigDecimal getCaloriesPerDay(UserView user, BigDecimal BMR) {
    return switch (user.workoutState()) {
      case SEDENTARY -> BMR.multiply(new BigDecimal("1.2"));
      case LIGHTLY_ACTIVE -> BMR.multiply(new BigDecimal("1.375"));
      case MODERATELY_ACTIVE -> BMR.multiply(new BigDecimal("1.55"));
      case VERY_ACTIVE -> BMR.multiply(new BigDecimal("1.725"));
      case SUPER_ACTIVE -> BMR.multiply(new BigDecimal("1.9"));
    };
  }

  public static BigDecimal getBmr(UserView user) {
    BigDecimal BMR;

    if (Gender.MALE == user.gender()) {
      BMR = new BigDecimal("88.362")
          .add(new BigDecimal("13.397").multiply(BigDecimal.valueOf(user.kilograms())))
          .add(new BigDecimal("4.799").multiply(BigDecimal.valueOf(user.height())))
          .subtract(new BigDecimal("5.677")
              .add(new BigDecimal(user.age())));
    } else {
      BMR = new BigDecimal("447.593")
          .add(new BigDecimal("9.247").multiply(BigDecimal.valueOf(user.kilograms())))
          .add(new BigDecimal("3.098").multiply(BigDecimal.valueOf(user.height())))
          .subtract(new BigDecimal("4.330")
              .add(new BigDecimal(user.age())));
    }
    return BMR;
  }

  public static String generateRandomNumbers(int num) {
    Random rand = new Random();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < num; i++) {
      int randomNum = rand.nextInt(100);
      sb.append(randomNum);
    }
    return sb.toString();
  }
}
