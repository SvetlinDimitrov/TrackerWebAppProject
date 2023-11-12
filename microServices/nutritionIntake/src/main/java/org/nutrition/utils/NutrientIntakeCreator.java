package org.nutrition.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.nutrition.clients.electrolyte.ElectrolyteClient;
import org.nutrition.clients.macronutrient.MacronutrientClient;
import org.nutrition.clients.vitamin.VitaminClient;
import org.nutrition.model.dtos.RecordCreation;
import org.nutrition.model.entity.NutritionIntake;
import org.nutrition.model.enums.Gender;
import org.nutrition.model.enums.WorkoutState;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NutrientIntakeCreator {

    private final MacronutrientClient macronutrientClient;
    private final VitaminClient vitaminClient;
    private final ElectrolyteClient electrolyteClient;

    public List<NutritionIntake> create(RecordCreation createDto) {
        List<NutritionIntake> nutritionIntakeEntities = new ArrayList<>();

        fillAllVitaminsRecords(createDto.getGender(), createDto.getRecordId(), nutritionIntakeEntities);
        fillAllElectrolytesRecords(createDto.getGender(), createDto.getRecordId(), nutritionIntakeEntities);
        fillAllMacronutrientRecords(createDto, createDto.getRecordId(), nutritionIntakeEntities);

        return nutritionIntakeEntities;
    }

    private void fillAllMacronutrientRecords(RecordCreation createDto, Long recordId,
            List<NutritionIntake> nutritionIntakeEntities) {
        macronutrientClient
                .getAllMacronutrients()
                .forEach(macro -> {
                    BigDecimal lowerBoundIntake = inactiveState(createDto.getWorkoutState())
                            ? createDto.getCaloriesPerDay().multiply(BigDecimal.valueOf(macro.getInactiveState()))
                            : createDto.getCaloriesPerDay().multiply(BigDecimal.valueOf(macro.getActiveState()));

                    BigDecimal upperBoundIntake = inactiveState(createDto.getWorkoutState())
                            ? createDto.getCaloriesPerDay().multiply(BigDecimal.valueOf(macro.getInactiveState()))
                            : createDto.getCaloriesPerDay().multiply(BigDecimal.valueOf(macro.getActiveState()));

                    if (macro.getName().equals("Fat")) {
                        upperBoundIntake = (upperBoundIntake.divide(new BigDecimal("9"), RoundingMode.HALF_UP));
                        lowerBoundIntake = (lowerBoundIntake.divide(new BigDecimal("9"), RoundingMode.HALF_UP));
                    } else {
                        upperBoundIntake = (upperBoundIntake.divide(new BigDecimal("4"), RoundingMode.HALF_UP));
                        lowerBoundIntake = (lowerBoundIntake.divide(new BigDecimal("4"), RoundingMode.HALF_UP));
                    }
                    NutritionIntake nutritionIntake = createNutrition(macro.getName(),
                            "Macronutrient",
                            "grams (g)",
                            lowerBoundIntake,
                            upperBoundIntake,
                            recordId);

                    nutritionIntakeEntities.add(nutritionIntake);
                });
    }

    private void fillAllVitaminsRecords(Gender gender, Long recordId, List<NutritionIntake> nutritionIntakeEntities) {
        vitaminClient
                .getAllVitamins()
                .forEach(vitamin -> {
                    BigDecimal lowerBoundIntake = gender.equals(Gender.MALE) ? vitamin.getMaleLowerBoundIntake()
                            : vitamin.getFemaleLowerBoundIntake();
                    BigDecimal upperBoundIntake = gender.equals(Gender.MALE) ? vitamin.getMaleHigherBoundIntake()
                            : vitamin.getFemaleHigherBoundIntake();
                    nutritionIntakeEntities.add(
                            createNutrition(
                                    vitamin.getName(),
                                    "Vitamin",
                                    vitamin.getMeasure(),
                                    lowerBoundIntake,
                                    upperBoundIntake,
                                    recordId));
                });
    }

    private void fillAllElectrolytesRecords(Gender gender, Long recordId,
            List<NutritionIntake> nutritionIntakeEntities) {
        electrolyteClient
                .getAllElectrolytes()
                .forEach(electrolyte -> {
                    BigDecimal lowerBoundIntake = gender.equals(Gender.MALE) ? electrolyte.getMaleLowerBoundIntake()
                            : electrolyte.getFemaleLowerBoundIntake();
                    BigDecimal upperBoundIntake = gender.equals(Gender.MALE) ? electrolyte.getMaleHigherBoundIntake()
                            : electrolyte.getFemaleHigherBoundIntake();
                    nutritionIntakeEntities.add(
                            createNutrition(electrolyte.getName(),
                                    "Electrolyte",
                                    electrolyte.getMeasure(),
                                    lowerBoundIntake,
                                    upperBoundIntake,
                                    recordId));
                });
    }

    private Boolean inactiveState(WorkoutState state) {
        return state.equals(WorkoutState.SEDENTARY) || state.equals(WorkoutState.LIGHTLY_ACTIVE);
    }

    private NutritionIntake createNutrition(String nutrientName, String nutrientType, String measurement,
            BigDecimal lowerBoundIntake, BigDecimal upperBoundIntake, Long recordId) {

        return NutritionIntake.builder()
                .nutrientName(nutrientName)
                .nutrientType(nutrientType)
                .measurement(measurement)
                .lowerBoundIntake(lowerBoundIntake)
                .upperBoundIntake(upperBoundIntake)
                .recordId(recordId)
                .dailyConsumed(BigDecimal.ZERO)
                .build();
    }
}
