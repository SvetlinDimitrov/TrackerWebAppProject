package org.trackerwebapp.trackerwebapp.utils;

import org.trackerwebapp.trackerwebapp.domain.enums.Gender;

import java.math.BigDecimal;

public class BMRCalc {

  /**
   * Calculate Basal Metabolic Rate (BMR): This is the number of calories your body needs to
   * maintain basic physiological functions while at rest. The most common formula for calculating
   * BMR is the Harris-Benedict equation. There are separate formulas for men and women:
   * <p>
   * For men: BMR = 88.362 + (13.397 × weight in kg) + (4.799 × height in cm) - (5.677 × age in
   * years) For women: BMR = 447.593 + (9.247 × weight in kg) + (3.098 × height in cm) - (4.330 ×
   * age in years)
   **/
  public static BigDecimal calculateBMR(Gender gender, BigDecimal kg, BigDecimal height, Integer age) {
    BigDecimal BMR;

    if (gender.equals(Gender.MALE)) {
      BMR = new BigDecimal("88.362")
          .add(new BigDecimal("13.397").multiply(kg))
          .add(new BigDecimal("4.799").multiply(height))
          .subtract(new BigDecimal("5.677").multiply(new BigDecimal(age)));
    } else {
      BMR = new BigDecimal("447.593")
          .add(new BigDecimal("9.247").multiply(kg))
          .add(new BigDecimal("3.098").multiply(height))
          .subtract(new BigDecimal("4.330").multiply(new BigDecimal(age)));
    }
    return BMR;
  }
}
