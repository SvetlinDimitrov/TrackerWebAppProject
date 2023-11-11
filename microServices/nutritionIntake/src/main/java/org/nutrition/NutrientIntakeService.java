package org.nutrition;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.nutrition.clients.electrolyte.ElectrolyteClient;
import org.nutrition.clients.macronutrient.MacronutrientClient;
import org.nutrition.clients.vitamin.VitaminClient;
import org.nutrition.exceptions.RecordNotFoundException;
import org.nutrition.model.dtos.FoodView;
import org.nutrition.model.dtos.NutritionIntakeView;
import org.nutrition.model.dtos.RecordCreation;
import org.nutrition.model.entity.NutritionIntake;
import org.nutrition.model.enums.Gender;
import org.nutrition.model.enums.WorkoutState;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NutrientIntakeService {
    private final NutritionIntakeRepository repository;
    private final VitaminClient vitaminClient;
    private final ElectrolyteClient electrolyteClient;
    private final MacronutrientClient macronutrientClient;

    @KafkaListener(topics = "record-creation", groupId = "group_one", containerFactory = "kafkaListenerCreation")
    public void create(@Payload RecordCreation createDto) {
        List<NutritionIntake> nutritionIntakeEntities = new ArrayList<>();

        fillAllVitaminsRecords(createDto.getGender(), createDto.getRecordId(), nutritionIntakeEntities);
        fillAllElectrolytesRecords(createDto.getGender(), createDto.getRecordId(), nutritionIntakeEntities);
        fillAllMacronutrientRecords(createDto, createDto.getRecordId(), nutritionIntakeEntities);

        repository.saveAll(nutritionIntakeEntities);
    }

    @KafkaListener(topics = "record-deletion", groupId = "group_two", containerFactory = "kafkaListenerDeletion")
    @Transactional
    public void deleteNutritionIntakesByRecordId(@Payload Long recordId) {
        repository.deleteAllByRecordId(recordId);
    }

    public List<NutritionIntakeView> getAllNutritionIntakeByRecordId(Long recordId) throws RecordNotFoundException {
        List<NutritionIntake> nutritionIntakes = repository.findAllByRecordId(recordId).get();
        if (nutritionIntakes.isEmpty()) {
            throw new RecordNotFoundException("No nutritionIntakes was found with id:" + recordId);
        }
        return nutritionIntakes
                .stream()
                .map(this::toNutritionIntakeView)
                .collect(Collectors.toList());
    }

    @KafkaListener(topics = "storage-filling", groupId = "group_storage_filling_1", containerFactory = "kafkaListenerStorageFilling")
    @Transactional
    public void changeNutritionIntake(FoodView foodView) throws RecordNotFoundException {

        List<NutritionIntake> nutritionIntake = repository
                .findAllByRecordId(foodView.getRecordId())
                .orElseThrow(() -> new RecordNotFoundException(
                        "No nutritionIntakes was found with id:" + foodView.getRecordId()));

        nutritionIntake.forEach(entity -> fillNutritionChanges(entity, foodView));
        repository.saveAll(nutritionIntake);

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

    private Boolean inactiveState(WorkoutState state) {
        return state.equals(WorkoutState.SEDENTARY) || state.equals(WorkoutState.LIGHTLY_ACTIVE);
    }

    private NutritionIntakeView toNutritionIntakeView(NutritionIntake entity) {
        return new NutritionIntakeView(entity.getId(),
                entity.getNutrientName(),
                entity.getNutrientType(),
                entity.getDailyConsumed(),
                entity.getLowerBoundIntake(),
                entity.getUpperBoundIntake(),
                entity.getMeasurement(),
                entity.getRecordId());
    }

    private void fillNutritionChanges(NutritionIntake entity, FoodView foodView) {
        if (entity.getNutrientName().equals("A") && foodView.getA() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getA()));
        } else if (entity.getNutrientName().equals("D") && foodView.getD() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getD()));
        } else if (entity.getNutrientName().equals("E") && foodView.getE() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getE()));
        } else if (entity.getNutrientName().equals("K") && foodView.getK() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getK()));
        } else if (entity.getNutrientName().equals("B1") && foodView.getB1() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getB1()));
        } else if (entity.getNutrientName().equals("B2") && foodView.getB2() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getB2()));
        } else if (entity.getNutrientName().equals("B3") && foodView.getB3() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getB3()));
        } else if (entity.getNutrientName().equals("B5") && foodView.getB5() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getB5()));
        } else if (entity.getNutrientName().equals("B6") && foodView.getB6() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getB6()));
        } else if (entity.getNutrientName().equals("B7") && foodView.getB7() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getB7()));
        } else if (entity.getNutrientName().equals("B9") && foodView.getB9() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getB9()));
        } else if (entity.getNutrientName().equals("B12") && foodView.getB12() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getB12()));
        } else if (entity.getNutrientName().equals("C") && foodView.getC() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getC()));
        } else if (entity.getNutrientName().equals("Calcium") && foodView.getCalcium() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getCalcium()));
        } else if (entity.getNutrientName().equals("Chloride") && foodView.getChloride() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getChloride()));
        } else if (entity.getNutrientName().equals("Magnesium") && foodView.getMagnesium() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getMagnesium()));
        } else if (entity.getNutrientName().equals("Potassium") && foodView.getPotassium() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getPotassium()));
        } else if (entity.getNutrientName().equals("Sodium") && foodView.getSodium() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getSodium()));
        } else if (entity.getNutrientName().equals("Fat") && foodView.getFat() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getFat()));
        } else if (entity.getNutrientName().equals("Carbohydrates") && foodView.getCarbohydrates() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getCarbohydrates()));
        } else if (entity.getNutrientName().equals("Protein") && foodView.getProtein() != null) {
            entity.setDailyConsumed(entity.getDailyConsumed().add(foodView.getProtein()));
        }
    }
}
