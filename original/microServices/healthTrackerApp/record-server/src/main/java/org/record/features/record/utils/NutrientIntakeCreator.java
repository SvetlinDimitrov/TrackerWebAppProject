package org.record.features.record.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.domain.record.enums.MacronutrientSpecificationEnum;
import org.example.domain.record.enums.MineralSpecificationEnum;
import org.example.domain.record.enums.VitaminSpecificationEnum;
import org.example.domain.user.enums.Gender;
import org.example.domain.user.enums.WorkoutState;
import org.example.domain.record.dtos.NutritionIntakeView;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NutrientIntakeCreator {

  public Map<String, NutritionIntakeView> create(Gender gender, BigDecimal caloriesPerDay,
      WorkoutState workoutState) {

    Map<String, NutritionIntakeView> nutritionIntakeEntities = new LinkedHashMap<>();

    fillAllVitaminsRecords(gender, nutritionIntakeEntities);
    fillAllMineralsRecords(gender, nutritionIntakeEntities);
    fillAllMacronutrientRecords(gender, caloriesPerDay, workoutState, nutritionIntakeEntities);
    fillMacroTypes(
        nutritionIntakeEntities,
        nutritionIntakeEntities.get("Fat").getLowerBoundIntake(),
        caloriesPerDay,
        gender);

    return nutritionIntakeEntities;
  }

  private void fillAllMacronutrientRecords(
      Gender gender,
      BigDecimal caloriesPerDay,
      WorkoutState workoutState,
      Map<String, NutritionIntakeView> nutritionIntakeEntities) {

    Arrays.stream(MacronutrientSpecificationEnum.values())
        .forEach(macro -> {
          BigDecimal lowerBoundIntake = inactiveState(workoutState)
              ? caloriesPerDay.multiply(
              BigDecimal.valueOf(macro.getInactiveState()))
              : caloriesPerDay.multiply(
                  BigDecimal.valueOf(macro.getActiveState()));

          BigDecimal upperBoundIntake = inactiveState(workoutState)
              ? caloriesPerDay.multiply(
              BigDecimal.valueOf(macro.getInactiveState()))
              : caloriesPerDay.multiply(
                  BigDecimal.valueOf(macro.getActiveState()));

          if (macro.getName().equals("Fat")) {
            upperBoundIntake = (upperBoundIntake.divide(new BigDecimal("9"),
                RoundingMode.HALF_UP));
            lowerBoundIntake = (lowerBoundIntake.divide(new BigDecimal("9"),
                RoundingMode.HALF_UP));
          } else {
            upperBoundIntake = (upperBoundIntake.divide(new BigDecimal("4"),
                RoundingMode.HALF_UP));
            lowerBoundIntake = (lowerBoundIntake.divide(new BigDecimal("4"),
                RoundingMode.HALF_UP));
          }
          NutritionIntakeView nutritionIntake = createNutrition(macro.getName(),
              "Macronutrient",
              "grams (g)",
              lowerBoundIntake,
              upperBoundIntake);

          nutritionIntakeEntities.put(nutritionIntake.getNutrientName(), nutritionIntake);
        });
  }

  private void fillAllVitaminsRecords(
      Gender gender,
      Map<String, NutritionIntakeView> nutritionIntakeEntities) {
    Arrays.stream(VitaminSpecificationEnum.values())
        .forEach(vitamin -> {
          BigDecimal lowerBoundIntake = gender.equals(Gender.MALE)
              ? BigDecimal.valueOf(vitamin.getMaleLowerBound())
              : BigDecimal.valueOf(vitamin.getFemaleLowerBound());
          BigDecimal upperBoundIntake = gender.equals(Gender.MALE)
              ? BigDecimal.valueOf(vitamin.getMaleUpperBound())
              : BigDecimal.valueOf(vitamin.getFemaleUpperBound());
          NutritionIntakeView nutrient = createNutrition(vitamin.getName(),
              "Vitamin",
              vitamin.getMeasure(),
              lowerBoundIntake,
              upperBoundIntake);

          nutritionIntakeEntities.put(nutrient.getNutrientName(), nutrient);
        });
  }

  private void fillAllMineralsRecords(
      Gender gender,
      Map<String, NutritionIntakeView> nutritionIntakeEntities) {
    Arrays.stream(MineralSpecificationEnum.values())
        .forEach(mineral -> {
          BigDecimal lowerBoundIntake = gender.equals(Gender.MALE)
              ? BigDecimal.valueOf(mineral.getMaleLowerBound())
              : BigDecimal.valueOf(mineral.getFemaleLowerBound());
          BigDecimal upperBoundIntake = gender.equals(Gender.MALE)
              ? BigDecimal.valueOf(mineral.getMaleUpperBound())
              : BigDecimal.valueOf(mineral.getFemaleUpperBound());

          NutritionIntakeView nutrient = createNutrition(mineral.getName(),
              "Mineral",
              mineral.getMeasure(),
              lowerBoundIntake,
              upperBoundIntake);
          nutritionIntakeEntities.put(nutrient.getNutrientName(), nutrient);

        });
  }

  private Boolean inactiveState(WorkoutState state) {
    return state.equals(WorkoutState.SEDENTARY) || state.equals(WorkoutState.LIGHTLY_ACTIVE);
  }

  private NutritionIntakeView createNutrition(
      String nutrientName,
      String nutrientType,
      String measurement,
      BigDecimal lowerBoundIntake,
      BigDecimal upperBoundIntake) {

    return NutritionIntakeView.builder()
        .nutrientName(nutrientName)
        .nutrientType(nutrientType)
        .measurement(measurement)
        .lowerBoundIntake(lowerBoundIntake)
        .upperBoundIntake(upperBoundIntake)
        .dailyConsumed(BigDecimal.ZERO)
        .build();
  }

  private void fillMacroTypes(
      Map<String, NutritionIntakeView> nutritionIntakeEntities,
      BigDecimal fat,
      BigDecimal totalCalories,
      Gender gender) {

    NutritionIntakeView saturatedFat = NutritionIntakeView.builder()
        .nutrientName("Saturated Fat")
        .nutrientType("Fat")
        .measurement("g")
        .lowerBoundIntake(fat.multiply(BigDecimal.valueOf(0.1)))
        .upperBoundIntake(fat.multiply(BigDecimal.valueOf(0.1)))
        .dailyConsumed(BigDecimal.ZERO)
        .build();
    nutritionIntakeEntities.put(saturatedFat.getNutrientName(), saturatedFat);

    NutritionIntakeView monoFat = NutritionIntakeView.builder()
        .nutrientName("Monounsaturated Fat")
        .nutrientType("Fat")
        .measurement("g")
        .lowerBoundIntake(fat.multiply(BigDecimal.valueOf(0.15)))
        .upperBoundIntake(fat.multiply(BigDecimal.valueOf(0.2)))
        .dailyConsumed(BigDecimal.ZERO)
        .build();
    nutritionIntakeEntities.put(monoFat.getNutrientName(), monoFat);

    NutritionIntakeView polyFat = NutritionIntakeView.builder()
        .nutrientName("Polyunsaturated Fat")
        .nutrientType("Fat")
        .measurement("g")
        .lowerBoundIntake(fat.multiply(BigDecimal.valueOf(0.05)))
        .upperBoundIntake(fat.multiply(BigDecimal.valueOf(0.1)))
        .dailyConsumed(BigDecimal.ZERO)
        .build();
    nutritionIntakeEntities.put(polyFat.getNutrientName(), polyFat);

    NutritionIntakeView transFat = NutritionIntakeView.builder()
        .nutrientName("Trans Fat")
        .nutrientType("Fat")
        .measurement("g")
        .lowerBoundIntake(BigDecimal.ZERO)
        .upperBoundIntake(BigDecimal.ZERO)
        .dailyConsumed(BigDecimal.ZERO)
        .build();
    nutritionIntakeEntities.put(transFat.getNutrientName(), transFat);

    NutritionIntakeView sugarCarbs = NutritionIntakeView.builder()
        .nutrientName("Sugar")
        .nutrientType("Carbohydrates")
        .measurement("g")
        .lowerBoundIntake(totalCalories
            .multiply(BigDecimal.valueOf(0.05).divide(BigDecimal.valueOf(4), 2,
                RoundingMode.HALF_UP)))
        .upperBoundIntake(totalCalories
            .multiply(BigDecimal.valueOf(0.1).divide(BigDecimal.valueOf(4), 2,
                RoundingMode.HALF_UP)))
        .dailyConsumed(BigDecimal.ZERO)
        .build();
    nutritionIntakeEntities.put(sugarCarbs.getNutrientName(), sugarCarbs);

    BigDecimal lowerBoundIntake =
        gender == Gender.MALE ? BigDecimal.valueOf(38) : BigDecimal.valueOf(25);

    NutritionIntakeView fiberCarbs = NutritionIntakeView.builder()
        .nutrientName("Fiber")
        .nutrientType("Carbohydrates")
        .measurement("g")
        .lowerBoundIntake(
            lowerBoundIntake)
        .upperBoundIntake(
            lowerBoundIntake)
        .dailyConsumed(BigDecimal.ZERO)
        .build();
    nutritionIntakeEntities.put(fiberCarbs.getNutrientName(), fiberCarbs);

  }

}
