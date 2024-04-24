package org.trackerwebapp.trackerwebapp.utils;

import org.trackerwebapp.trackerwebapp.domain.enums.AllowedNutrients;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculateMacros {

  /*
  Calculate Macros:
  Protein: 25% of 2946.647 calories = (0.25 × 2946.647) / 4 calories per gram = 183.29 grams
  Carbohydrates: 50% of 2946.647 calories = (0.50 × 2946.647) / 4 calories per gram = 367.50 grams
  Fats: 25% of 2946.647 calories = (0.25 × 2946.647) / 9 calories per gram = 109.29 grams
  */
  public static BigDecimal calculateMacros(String name, BigDecimal dailyConsumedCalories, BigDecimal distributedMacros) {
    BigDecimal raw = distributedMacros.multiply(dailyConsumedCalories);
    return name.equals(AllowedNutrients.Protein.getNutrientName()) ||
        name.equals(AllowedNutrients.Carbohydrate.getNutrientName()) ?
        raw.divide(BigDecimal.valueOf(4), RoundingMode.HALF_DOWN).setScale(0, RoundingMode.DOWN) :
        raw.divide(BigDecimal.valueOf(9), RoundingMode.HALF_DOWN).setScale(0, RoundingMode.DOWN);
  }
}
