package org.nutrition;

import lombok.RequiredArgsConstructor;
import org.nutrition.clients.electrolyte.ElectrolyteClient;
import org.nutrition.clients.macronutrient.MacronutrientClient;
import org.nutrition.clients.vitamin.VitaminClient;
import org.nutrition.exceptions.NutrientNameNotFoundException;
import org.nutrition.exceptions.RecordNotFoundException;
import org.nutrition.model.dtos.NutritionIntakeChangeDto;
import org.nutrition.model.dtos.RecordCreation;
import org.nutrition.model.dtos.NutritionIntakeView;
import org.nutrition.model.entity.NutritionIntake;
import org.nutrition.model.enums.Gender;
import org.nutrition.model.enums.WorkoutState;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


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
    public void deleteNutritionIntakesByRecordId(@Payload Long recordId){
        repository.deleteAllByRecordId(recordId);
    }

    public List<NutritionIntakeView> getAllNutritionIntakeByRecordId(Long recordId) throws RecordNotFoundException {
        List<NutritionIntake> nutritionIntakes = repository.findAllByRecordId(recordId);
        if (nutritionIntakes.isEmpty()) {
            throw new RecordNotFoundException("No nutritionIntakes was found with id:" + recordId);
        }
        return nutritionIntakes
                .stream()
                .map(this::toNutritionIntakeView)
                .collect(Collectors.toList());
    }


    @Transactional
    public NutritionIntakeView changeNutritionIntake(Long recordId, NutritionIntakeChangeDto changeDto) throws NutrientNameNotFoundException {

        NutritionIntake nutritionIntake = repository.findByRecordIdAndNutrientName(recordId, changeDto.getNutritionName())
                .orElseThrow(() -> new NutrientNameNotFoundException("There is no nutrition with the give name :" +changeDto.getNutritionName()));

        nutritionIntake.setDailyConsumed(nutritionIntake.getDailyConsumed().add(changeDto.getDailyConsumed()));
        repository.saveAndFlush(nutritionIntake);

        return toNutritionIntakeView(nutritionIntake);
    }private void fillAllMacronutrientRecords(RecordCreation createDto, Long recordId, List<NutritionIntake> nutritionIntakeEntities) {
        macronutrientClient
                .getAllMacronutrients()
                .forEach(macro -> {
                    BigDecimal lowerBoundIntake =
                            inactiveState(createDto.getWorkoutState()) ?
                                    createDto.getCaloriesPerDay().multiply(BigDecimal.valueOf(macro.getInactiveState()))
                                    : createDto.getCaloriesPerDay().multiply(BigDecimal.valueOf(macro.getActiveState()));

                    BigDecimal upperBoundIntake =
                            inactiveState(createDto.getWorkoutState()) ?
                                    createDto.getCaloriesPerDay().multiply(BigDecimal.valueOf(macro.getInactiveState()))
                                    : createDto.getCaloriesPerDay().multiply(BigDecimal.valueOf(macro.getActiveState()))
                            ;

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

    private void fillAllElectrolytesRecords(Gender gender, Long recordId, List<NutritionIntake> nutritionIntakeEntities) {
        electrolyteClient
                .getAllElectrolytes()
                .forEach(electrolyte -> {
                    BigDecimal lowerBoundIntake = gender.equals(Gender.MALE) ? electrolyte.getMaleLowerBoundIntake() : electrolyte.getFemaleLowerBoundIntake();
                    BigDecimal upperBoundIntake = gender.equals(Gender.MALE) ? electrolyte.getMaleHigherBoundIntake() : electrolyte.getFemaleHigherBoundIntake();
                    nutritionIntakeEntities.add(
                            createNutrition(electrolyte.getName(),
                                    "Electrolyte",
                                    electrolyte.getMeasure(),
                                    lowerBoundIntake,
                                    upperBoundIntake,
                                    recordId)
                    );
                });
    }
    private void fillAllVitaminsRecords(Gender gender, Long recordId, List<NutritionIntake> nutritionIntakeEntities) {
        vitaminClient
                .getAllVitamins()
                .forEach(vitamin -> {
                    BigDecimal lowerBoundIntake = gender.equals(Gender.MALE) ? vitamin.getMaleLowerBoundIntake() : vitamin.getFemaleLowerBoundIntake();
                    BigDecimal upperBoundIntake = gender.equals(Gender.MALE) ? vitamin.getMaleHigherBoundIntake() : vitamin.getFemaleHigherBoundIntake();
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
}
