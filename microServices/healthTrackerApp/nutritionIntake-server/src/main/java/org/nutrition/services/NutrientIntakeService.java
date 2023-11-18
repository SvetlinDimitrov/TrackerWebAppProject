package org.nutrition.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.nutrition.NutritionIntakeRepository;
import org.nutrition.exceptions.NutritionCreateException;
import org.nutrition.exceptions.RecordNotFoundException;
import org.nutrition.model.dtos.NutritionIntakeView;
import org.nutrition.model.dtos.RecordCreation;
import org.nutrition.model.entity.NutritionIntake;
import org.nutrition.model.enums.Gender;
import org.nutrition.model.enums.WorkoutState;
import org.nutrition.utils.NutrientIntakeCreator;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NutrientIntakeService {

    private final NutritionIntakeRepository repository;
    private final NutrientIntakeCreator creator;

    public List<NutritionIntakeView> createNutritionIntake(
            Long recordId,
            Gender gender,
            BigDecimal caloriesPerDay,
            WorkoutState workoutState) throws NutritionCreateException {

                RecordCreation recordCreation = creator.validateNutritionIntake(recordId, gender, caloriesPerDay, workoutState);
                List<NutritionIntake> nutritionIntakes = creator.create(recordCreation);
                repository.saveAll(nutritionIntakes);

        return nutritionIntakes.stream()
                .map(this::toNutritionIntakeView)
                .collect(Collectors.toList());
    }

    public List<NutritionIntakeView> getAllNutritionIntakeByRecordId(Long recordId) throws RecordNotFoundException {

        List<NutritionIntake> nutritionIntakes = repository.findAllByRecordId(recordId).get();

        if (nutritionIntakes.isEmpty()) {
            throw new RecordNotFoundException("No nutrition intake found for record id: " + recordId);
        }

        return nutritionIntakes
                .stream()
                .map(this::toNutritionIntakeView)
                .collect(Collectors.toList());
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
