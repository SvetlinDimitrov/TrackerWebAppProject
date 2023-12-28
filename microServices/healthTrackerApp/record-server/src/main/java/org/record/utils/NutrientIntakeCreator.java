package org.record.utils;

import lombok.RequiredArgsConstructor;
import org.record.client.dto.MacronutrientDto;
import org.record.client.dto.MineralDto;
import org.record.client.dto.VitaminDto;
import org.record.model.entity.NutritionIntake;
import org.record.model.enums.Gender;
import org.record.model.enums.WorkoutState;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NutrientIntakeCreator {

    public Map<String, NutritionIntake> create(
            Gender gender,
            BigDecimal caloriesPerDay,
            WorkoutState workoutState) {

        Map<String, NutritionIntake> nutritionIntakeEntities = new LinkedHashMap<>();

        fillAllVitaminsRecords(
                gender,
                nutritionIntakeEntities);
        fillAllMineralsRecords(
                gender,
                nutritionIntakeEntities);
        fillAllMacronutrientRecords(
                gender,
                caloriesPerDay,
                workoutState,
                nutritionIntakeEntities);
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
            Map<String, NutritionIntake> nutritionIntakeEntities) {

        macronutrientNutrients()
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
                    NutritionIntake nutritionIntake = createNutrition(macro.getName(),
                            "Macronutrient",
                            "grams (g)",
                            lowerBoundIntake,
                            upperBoundIntake);

                    nutritionIntakeEntities.put(nutritionIntake.getNutrientName(), nutritionIntake);
                });
    }

    private void fillAllVitaminsRecords(
            Gender gender,
            Map<String, NutritionIntake> nutritionIntakeEntities) {
        vitaminNutrients()
                .forEach(vitamin -> {
                    BigDecimal lowerBoundIntake = gender.equals(Gender.MALE)
                            ? vitamin.getMaleLowerBoundIntake()
                            : vitamin.getFemaleLowerBoundIntake();
                    BigDecimal upperBoundIntake = gender.equals(Gender.MALE)
                            ? vitamin.getMaleHigherBoundIntake()
                            : vitamin.getFemaleHigherBoundIntake();
                    NutritionIntake nutrient = createNutrition(vitamin.getName(),
                            "Vitamin",
                            vitamin.getMeasure(),
                            lowerBoundIntake,
                            upperBoundIntake);

                    nutritionIntakeEntities.put(nutrient.getNutrientName(), nutrient);
                });
    }

    private void fillAllMineralsRecords(
            Gender gender,
            Map<String, NutritionIntake> nutritionIntakeEntities) {
        mineralNutrients()
                .forEach(mineral -> {
                    BigDecimal lowerBoundIntake = gender.equals(Gender.MALE)
                            ? mineral.getMaleLowerBoundIntake()
                            : mineral.getFemaleLowerBoundIntake();
                    BigDecimal upperBoundIntake = gender.equals(Gender.MALE)
                            ? mineral.getMaleHigherBoundIntake()
                            : mineral.getFemaleHigherBoundIntake();

                    NutritionIntake nutrient = createNutrition(mineral.getName(),
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

    private NutritionIntake createNutrition(
            String nutrientName,
            String nutrientType,
            String measurement,
            BigDecimal lowerBoundIntake,
            BigDecimal upperBoundIntake) {

        return NutritionIntake.builder()
                .nutrientName(nutrientName)
                .nutrientType(nutrientType)
                .measurement(measurement)
                .lowerBoundIntake(lowerBoundIntake)
                .upperBoundIntake(upperBoundIntake)
                .dailyConsumed(BigDecimal.ZERO)
                .build();
    }

    private List<VitaminDto> vitaminNutrients() {
        return Arrays.asList(
                VitaminDto.builder()
                        .name("Vitamin A")
                        .measure("µg")
                        .maleLowerBoundIntake(new BigDecimal("600"))
                        .maleHigherBoundIntake(new BigDecimal("900"))
                        .femaleLowerBoundIntake(new BigDecimal("600"))
                        .femaleHigherBoundIntake(new BigDecimal("900"))
                        .build(),
                VitaminDto.builder()
                        .name("Vitamin D (D2 + D3)")
                        .measure("µg")
                        .maleLowerBoundIntake(new BigDecimal("10"))
                        .maleHigherBoundIntake(new BigDecimal("20"))
                        .femaleLowerBoundIntake(new BigDecimal("10"))
                        .femaleHigherBoundIntake(new BigDecimal("20"))
                        .build(),
                VitaminDto.builder()
                        .name("Vitamin E")
                        .measure("mg")
                        .maleLowerBoundIntake(new BigDecimal("15"))
                        .maleHigherBoundIntake(new BigDecimal("15"))
                        .femaleLowerBoundIntake(new BigDecimal("15"))
                        .femaleHigherBoundIntake(new BigDecimal("15"))
                        .build(),
                VitaminDto.builder()
                        .name("Vitamin K")
                        .measure("µg")
                        .maleLowerBoundIntake(new BigDecimal("120"))
                        .maleHigherBoundIntake(new BigDecimal("120"))
                        .femaleLowerBoundIntake(new BigDecimal("90"))
                        .femaleHigherBoundIntake(new BigDecimal("90"))
                        .build(),
                VitaminDto.builder()
                        .name("Vitamin C")
                        .measure("mg")
                        .maleLowerBoundIntake(new BigDecimal("90"))
                        .maleHigherBoundIntake(new BigDecimal("90"))
                        .femaleLowerBoundIntake(new BigDecimal("75"))
                        .femaleHigherBoundIntake(new BigDecimal("75"))
                        .build(),
                VitaminDto.builder()
                        .name("Vitamin B1 (Thiamin)")
                        .measure("mg")
                        .maleLowerBoundIntake(new BigDecimal("1.2"))
                        .maleHigherBoundIntake(new BigDecimal("1.2"))
                        .femaleLowerBoundIntake(new BigDecimal("1.1"))
                        .femaleHigherBoundIntake(new BigDecimal("1.1"))
                        .build(),
                VitaminDto.builder()
                        .name("Vitamin B2 (Riboflavin)")
                        .measure("mg")
                        .maleLowerBoundIntake(new BigDecimal("1.3"))
                        .maleHigherBoundIntake(new BigDecimal("1.3"))
                        .femaleLowerBoundIntake(new BigDecimal("1.1"))
                        .femaleHigherBoundIntake(new BigDecimal("1.1"))
                        .build(),
                VitaminDto.builder()
                        .name("Vitamin B3 (Niacin)")
                        .measure("mg")
                        .maleLowerBoundIntake(new BigDecimal("16"))
                        .maleHigherBoundIntake(new BigDecimal("16"))
                        .femaleLowerBoundIntake(new BigDecimal("14"))
                        .femaleHigherBoundIntake(new BigDecimal("14"))
                        .build(),
                VitaminDto.builder()
                        .name("Vitamin B5 (Pantothenic acid)")
                        .measure("mg")
                        .maleLowerBoundIntake(new BigDecimal("5"))
                        .maleHigherBoundIntake(new BigDecimal("5"))
                        .femaleLowerBoundIntake(new BigDecimal("5"))
                        .femaleHigherBoundIntake(new BigDecimal("5"))
                        .build(),
                VitaminDto.builder()
                        .name("Vitamin B6")
                        .measure("mg")
                        .maleLowerBoundIntake(new BigDecimal("1.3"))
                        .maleHigherBoundIntake(new BigDecimal("1.3"))
                        .femaleLowerBoundIntake(new BigDecimal("1.3"))
                        .femaleHigherBoundIntake(new BigDecimal("1.3"))
                        .build(),
                VitaminDto.builder()
                        .name("Vitamin B7 (Biotin)")
                        .measure("µg")
                        .maleLowerBoundIntake(new BigDecimal("30"))
                        .maleHigherBoundIntake(new BigDecimal("30"))
                        .femaleLowerBoundIntake(new BigDecimal("30"))
                        .femaleHigherBoundIntake(new BigDecimal("30"))
                        .build(),
                VitaminDto.builder()
                        .name("Vitamin B9 (Folate)")
                        .measure("µg")
                        .maleLowerBoundIntake(new BigDecimal("400"))
                        .maleHigherBoundIntake(new BigDecimal("400"))
                        .femaleLowerBoundIntake(new BigDecimal("400"))
                        .femaleHigherBoundIntake(new BigDecimal("400"))
                        .build(),
                VitaminDto.builder()
                        .name("Vitamin B12")
                        .measure("µg")
                        .maleLowerBoundIntake(new BigDecimal("2.4"))
                        .maleHigherBoundIntake(new BigDecimal("2.4"))
                        .femaleLowerBoundIntake(new BigDecimal("2.4"))
                        .femaleHigherBoundIntake(new BigDecimal("2.4"))
                        .build()

        );
    }

    private List<MineralDto> mineralNutrients() {
        return List.of(
                MineralDto.builder()
                        .name("Iron , Fe")
                        .maleHigherBoundIntake(new BigDecimal("11"))
                        .maleLowerBoundIntake(new BigDecimal("8"))
                        .femaleHigherBoundIntake(new BigDecimal("11"))
                        .femaleLowerBoundIntake(new BigDecimal("8"))
                        .measure("mg")
                        .build(),
                MineralDto.builder()
                        .name("Zinc , Zn")
                        .maleHigherBoundIntake(new BigDecimal("11"))
                        .maleLowerBoundIntake(new BigDecimal("11"))
                        .femaleHigherBoundIntake(new BigDecimal("8"))
                        .femaleLowerBoundIntake(new BigDecimal("8"))
                        .measure("mg")
                        .build(),
                MineralDto.builder()
                        .name("Copper , Cu")
                        .maleHigherBoundIntake(new BigDecimal("2.3"))
                        .maleLowerBoundIntake(new BigDecimal("0.9"))
                        .femaleHigherBoundIntake(new BigDecimal("2.3"))
                        .femaleLowerBoundIntake(new BigDecimal("0.9"))
                        .measure("mg")
                        .build(),
                MineralDto.builder()
                        .name("Manganese , Mn")
                        .maleHigherBoundIntake(new BigDecimal("11"))
                        .maleLowerBoundIntake(new BigDecimal("2.3"))
                        .femaleHigherBoundIntake(new BigDecimal("11"))
                        .femaleLowerBoundIntake(new BigDecimal("1.8"))
                        .measure("mg")
                        .build(),
                MineralDto.builder()
                        .name("Iodine , I")
                        .maleHigherBoundIntake(new BigDecimal("290"))
                        .maleLowerBoundIntake(new BigDecimal("150"))
                        .femaleHigherBoundIntake(new BigDecimal("290"))
                        .femaleLowerBoundIntake(new BigDecimal("150"))
                        .measure("µg")
                        .build(),
                MineralDto.builder()
                        .name("Selenium , Se")
                        .maleHigherBoundIntake(new BigDecimal("400"))
                        .maleLowerBoundIntake(new BigDecimal("55"))
                        .femaleHigherBoundIntake(new BigDecimal("400"))
                        .femaleLowerBoundIntake(new BigDecimal("55"))
                        .measure("µg")
                        .build(),
                MineralDto.builder()
                        .name("Molybdenum , Mo")
                        .maleHigherBoundIntake(new BigDecimal("2000"))
                        .maleLowerBoundIntake(new BigDecimal("45"))
                        .femaleHigherBoundIntake(new BigDecimal("2000"))
                        .femaleLowerBoundIntake(new BigDecimal("45"))
                        .measure("µg")
                        .build(),
                MineralDto.builder()
                        .name("Calcium , Ca")
                        .maleHigherBoundIntake(new BigDecimal("1300"))
                        .maleLowerBoundIntake(new BigDecimal("1000"))
                        .femaleHigherBoundIntake(new BigDecimal("1300"))
                        .femaleLowerBoundIntake(new BigDecimal("1000"))
                        .measure("mg")
                        .build(),
                MineralDto.builder()
                        .name("Phosphorus , P")
                        .maleHigherBoundIntake(new BigDecimal("1250"))
                        .maleLowerBoundIntake(new BigDecimal("700"))
                        .femaleHigherBoundIntake(new BigDecimal("1250"))
                        .femaleLowerBoundIntake(new BigDecimal("700"))
                        .measure("mg")
                        .build(),
                MineralDto.builder()
                        .name("Magnesium , Mg")
                        .maleHigherBoundIntake(new BigDecimal("420"))
                        .maleLowerBoundIntake(new BigDecimal("400"))
                        .femaleHigherBoundIntake(new BigDecimal("320"))
                        .femaleLowerBoundIntake(new BigDecimal("310"))
                        .measure("mg")
                        .build(),
                MineralDto.builder()
                        .name("Sodium , Na")
                        .maleHigherBoundIntake(new BigDecimal("2300"))
                        .maleLowerBoundIntake(new BigDecimal("2300"))
                        .femaleHigherBoundIntake(new BigDecimal("2300"))
                        .femaleLowerBoundIntake(new BigDecimal("2300"))
                        .measure("mg")
                        .build(),
                MineralDto.builder()
                        .name("Potassium , K")
                        .maleHigherBoundIntake(new BigDecimal("4700"))
                        .maleLowerBoundIntake(new BigDecimal("3500"))
                        .femaleHigherBoundIntake(new BigDecimal("4700"))
                        .femaleLowerBoundIntake(new BigDecimal("3500"))
                        .measure("mg")
                        .build()

        );
    }

    private List<MacronutrientDto> macronutrientNutrients() {
        return List.of(
                MacronutrientDto.builder()
                        .name("Carbohydrates")
                        .activeState(0.5)
                        .inactiveState(0.5)
                        .build(),
                MacronutrientDto.builder()
                        .name("Protein")
                        .activeState(0.25)
                        .inactiveState(0.15)
                        .build(),
                MacronutrientDto.builder()
                        .name("Fat")
                        .activeState(0.2)
                        .inactiveState(0.35)
                        .build());
    }

    private void fillMacroTypes(
            Map<String, NutritionIntake> nutritionIntakeEntities,
            BigDecimal fat,
            BigDecimal totalCalories,
            Gender gender) {

        NutritionIntake saturatedFat = NutritionIntake.builder()
                .nutrientName("Saturated Fat")
                .nutrientType("Fat")
                .measurement("g")
                .lowerBoundIntake(fat.multiply(BigDecimal.valueOf(0.1)))
                .upperBoundIntake(fat.multiply(BigDecimal.valueOf(0.1)))
                .dailyConsumed(BigDecimal.ZERO)
                .build();
        nutritionIntakeEntities.put(saturatedFat.getNutrientName(), saturatedFat);

        NutritionIntake monoFat = NutritionIntake.builder()
                .nutrientName("Monounsaturated Fat")
                .nutrientType("Fat")
                .measurement("g")
                .lowerBoundIntake(fat.multiply(BigDecimal.valueOf(0.15)))
                .upperBoundIntake(fat.multiply(BigDecimal.valueOf(0.2)))
                .dailyConsumed(BigDecimal.ZERO)
                .build();
        nutritionIntakeEntities.put(monoFat.getNutrientName(), monoFat);

        NutritionIntake polyFat = NutritionIntake.builder()
                .nutrientName("Polyunsaturated Fat")
                .nutrientType("Fat")
                .measurement("g")
                .lowerBoundIntake(fat.multiply(BigDecimal.valueOf(0.05)))
                .upperBoundIntake(fat.multiply(BigDecimal.valueOf(0.1)))
                .dailyConsumed(BigDecimal.ZERO)
                .build();
        nutritionIntakeEntities.put(polyFat.getNutrientName(), polyFat);

        NutritionIntake transFat = NutritionIntake.builder()
                .nutrientName("Trans Fat")
                .nutrientType("Fat")
                .measurement("g")
                .lowerBoundIntake(BigDecimal.ZERO)
                .upperBoundIntake(BigDecimal.ZERO)
                .dailyConsumed(BigDecimal.ZERO)
                .build();
        nutritionIntakeEntities.put(transFat.getNutrientName(), transFat);

        NutritionIntake sugarCarbs = NutritionIntake.builder()
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

        NutritionIntake fiberCarbs = NutritionIntake.builder()
                .nutrientName("Fiber")
                .nutrientType("Carbohydrates")
                .measurement("g")
                .lowerBoundIntake(
                        gender == Gender.MALE ? BigDecimal.valueOf(38) : BigDecimal.valueOf(25))
                .upperBoundIntake(
                        gender == Gender.MALE ? BigDecimal.valueOf(38) : BigDecimal.valueOf(25))
                .dailyConsumed(BigDecimal.ZERO)
                .build();
        nutritionIntakeEntities.put(fiberCarbs.getNutrientName(), fiberCarbs);

    }

}
