package org.record.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.record.RecordRepository;
import org.record.client.dto.Food;
import org.record.client.dto.Nutrient;
import org.record.client.dto.StorageView;
import org.record.client.dto.User;
import org.record.exceptions.InvalidJsonTokenException;
import org.record.exceptions.RecordNotFoundException;
import org.record.model.dtos.RecordView;
import org.record.model.entity.NutritionIntake;
import org.record.model.entity.Record;
import org.record.utils.GsonWrapper;
import org.record.utils.NutrientIntakeCreator;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public abstract class AbstractRecordService {

    protected final GsonWrapper gsonWrapper;
    protected final RecordRepository recordRepository;
    protected final NutrientIntakeCreator nutrientIntakeCreator;

    protected User getUser(String userToken) throws InvalidJsonTokenException {
        try{
            return gsonWrapper.fromJson(userToken, User.class);
        } catch (Exception e) {
           throw new InvalidJsonTokenException("Invalid user token.");
        }
    }

    protected RecordView toRecordView(Record record, List<StorageView> storages, User user) {
        RecordView recordView = RecordView.builder()
                .id(record.getId())
                .storageViews(storages)
                .dailyConsumedCalories(BigDecimal.ZERO)
                .dailyCaloriesToConsume(record.getDailyCalories())
                .userID(record.getUserId())
                .date(String.valueOf(record.getDate()))
                .name(record.getName())
                .build();

        Map<String, NutritionIntake> nutationsMap = nutrientIntakeCreator.create(
                user.getGender(),
                record.getDailyCalories(),
                user.getWorkoutState());

        calculateNutrientsAndDailyConsumedCalories(recordView, nutationsMap);

        recordView.setNutritionIntakesViews(nutationsMap.values().stream().toList());

        return recordView;
    }

    protected Record getRecordByIdAndUserId(String recordId, String userToken) throws RecordNotFoundException, InvalidJsonTokenException {
        String userId = getUser(userToken).getId();

        return recordRepository.findByIdAndUserId(recordId, userId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "Record with id " + recordId + " not found in user records."));
    }

    private void calculateNutrientsAndDailyConsumedCalories(
            RecordView record,
            Map<String, NutritionIntake> nutationsMap) {

        for (StorageView storage : record.getStorageViews()) {

            for (Food food : storage.getFoods()) {
                fillNutrientsMap(nutationsMap, food.getMineralNutrients());
                fillNutrientsMap(nutationsMap, food.getVitaminNutrients());
                fillNutrientsMap(nutationsMap, food.getMacronutrients());

            }
            if (storage.getConsumedCalories() != null) {
                record.setDailyConsumedCalories(
                        record.getDailyConsumedCalories().add(storage.getConsumedCalories()));
            }

        }

    }

    private void fillNutrientsMap(Map<String, NutritionIntake> nutationsMap, List<Nutrient> macronutrients) {
        for (Nutrient nutrient : macronutrients) {
            NutritionIntake nutritionIntake = nutationsMap.get(nutrient.getName());
            nutritionIntake.setDailyConsumed(nutritionIntake.getDailyConsumed().add(nutrient.getAmount()));
        }
    }

}
