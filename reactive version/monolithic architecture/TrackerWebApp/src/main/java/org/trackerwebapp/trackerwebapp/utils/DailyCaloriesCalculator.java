package org.trackerwebapp.trackerwebapp.utils;

import org.trackerwebapp.trackerwebapp.domain.enums.Goals;
import org.trackerwebapp.trackerwebapp.domain.enums.WorkoutState;

import java.math.BigDecimal;

public class DailyCaloriesCalculator {

  /*
    Factor in Activity Level: Multiply your BMR by an activity factor to get your Total Daily
    Energy Expenditure (TDEE). The activity factor typically ranges from 1.2 (sedentary) to 1.9
    (very active). Here's a general guideline:
    <p>
    Sedentary (little to no exercise): BMR × 1.2
    Lightly active (light exercise/sports 1-3days/week): BMR × 1.375
    Moderately active (moderate exercise/sports 3-5 days/week): BMR × 1.55
    Very active (hard exercise/sports 6-7 days a week): BMR × 1.725
    Extra active (very hard exercise/sports & physical job or 2x training): BMR × 1.9
   */

  /**
   * Set Goals: Determine whether the person wants to maintain, lose, or gain weight. Adjust their
   * calorie intake accordingly:
   * <p>
   * To maintain weight: TDEE To lose weight: TDEE - 500 calories/day (for about 1 pound per week
   * loss) To gain weight: TDEE + 500 calories/day (for about 1 pound per week gain)
   */
  public static BigDecimal getCaloriesPerDay(BigDecimal BMR, WorkoutState workoutState, Goals goal) {
    BigDecimal calories = switch (workoutState) {
      case WorkoutState.SEDENTARY -> BMR.multiply(new BigDecimal("1.2"));
      case WorkoutState.LIGHTLY_ACTIVE -> BMR.multiply(new BigDecimal("1.375"));
      case WorkoutState.MODERATELY_ACTIVE -> BMR.multiply(new BigDecimal("1.55"));
      case WorkoutState.VERY_ACTIVE -> BMR.multiply(new BigDecimal("1.725"));
      case WorkoutState.SUPER_ACTIVE -> BMR.multiply(new BigDecimal("1.9"));
    };
    return switch (goal) {
      case Goals.MaintainWeight -> calories;
      case Goals.LoseWeight -> calories.subtract(BigDecimal.valueOf(500));
      case Goals.GainWeight -> calories.add(BigDecimal.valueOf(500));
    };
  }
}
