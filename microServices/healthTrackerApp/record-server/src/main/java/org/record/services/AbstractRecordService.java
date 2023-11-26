package org.record.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.record.RecordRepository;
import org.record.client.dto.Food;
import org.record.client.dto.StorageView;
import org.record.client.dto.User;
import org.record.exceptions.RecordNotFoundException;
import org.record.model.dtos.RecordView;
import org.record.model.entity.NutritionIntake;
import org.record.model.entity.Record;
import org.record.utils.NutrientIntakeCreator;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public abstract class AbstractRecordService {

    protected final ObjectMapper objectMapper = new ObjectMapper();
    protected final RecordRepository recordRepository;
    protected final NutrientIntakeCreator nutrientIntakeCreator;

    protected User getUserId(String userToken) throws JsonProcessingException {
        return objectMapper.readValue(userToken, User.class);
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

    protected Record getRecordByIdAndUserId(Long recordId, String userToken) throws RecordNotFoundException, JsonProcessingException {
        Long userId = getUserId(userToken).getId();

        return recordRepository.findByIdAndUserId(recordId, userId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "Record with id " + recordId + " not found in user records."));
    }

    private void calculateNutrientsAndDailyConsumedCalories(
            RecordView record,
            Map<String, NutritionIntake> nutationsMap) {

        for (StorageView storage : record.getStorageViews()) {

            for (Food food : storage.getFoods()) {
                nutationsMap.get("A").setDailyConsumed(
                        nutationsMap.get("A").getDailyConsumed().add(food.getA()));
                nutationsMap.get("D").setDailyConsumed(
                        nutationsMap.get("D").getDailyConsumed().add(food.getD()));
                nutationsMap.get("E").setDailyConsumed(
                        nutationsMap.get("E").getDailyConsumed().add(food.getE()));
                nutationsMap.get("K").setDailyConsumed(
                        nutationsMap.get("K").getDailyConsumed().add(food.getK()));
                nutationsMap.get("C").setDailyConsumed(
                        nutationsMap.get("C").getDailyConsumed().add(food.getC()));
                nutationsMap.get("B1").setDailyConsumed(
                        nutationsMap.get("B1").getDailyConsumed().add(food.getB1()));
                nutationsMap.get("B2").setDailyConsumed(
                        nutationsMap.get("B2").getDailyConsumed().add(food.getB2()));
                nutationsMap.get("B3").setDailyConsumed(
                        nutationsMap.get("B3").getDailyConsumed().add(food.getB3()));
                nutationsMap.get("B5").setDailyConsumed(
                        nutationsMap.get("B5").getDailyConsumed().add(food.getB5()));
                nutationsMap.get("B6").setDailyConsumed(
                        nutationsMap.get("B6").getDailyConsumed().add(food.getB6()));
                nutationsMap.get("B7").setDailyConsumed(
                        nutationsMap.get("B7").getDailyConsumed().add(food.getB7()));
                nutationsMap.get("B9").setDailyConsumed(
                        nutationsMap.get("B9").getDailyConsumed().add(food.getB9()));
                nutationsMap.get("B12").setDailyConsumed(
                        nutationsMap.get("B12").getDailyConsumed().add(food.getB12()));

                nutationsMap.get("Calcium")
                        .setDailyConsumed(nutationsMap.get("Calcium").getDailyConsumed()
                                .add(food.getCalcium()));
                nutationsMap.get("Phosphorus")
                        .setDailyConsumed(nutationsMap.get("Phosphorus").getDailyConsumed()
                                .add(food.getPhosphorus()));
                nutationsMap.get("Magnesium")
                        .setDailyConsumed(nutationsMap.get("Magnesium").getDailyConsumed()
                                .add(food.getMagnesium()));
                nutationsMap.get("Sodium")
                        .setDailyConsumed(nutationsMap.get("Sodium").getDailyConsumed()
                                .add(food.getSodium()));
                nutationsMap.get("Potassium")
                        .setDailyConsumed(nutationsMap.get("Potassium").getDailyConsumed()
                                .add(food.getPotassium()));
                nutationsMap.get("Chloride")
                        .setDailyConsumed(nutationsMap.get("Chloride").getDailyConsumed()
                                .add(food.getChloride()));

                nutationsMap.get("Iron")
                        .setDailyConsumed(nutationsMap.get("Iron").getDailyConsumed()
                                .add(food.getIron()));
                nutationsMap.get("Zinc")
                        .setDailyConsumed(nutationsMap.get("Zinc").getDailyConsumed()
                                .add(food.getZinc()));
                nutationsMap.get("Copper")
                        .setDailyConsumed(nutationsMap.get("Copper").getDailyConsumed()
                                .add(food.getCopper()));
                nutationsMap.get("Manganese")
                        .setDailyConsumed(nutationsMap.get("Manganese").getDailyConsumed()
                                .add(food.getManganese()));
                nutationsMap.get("Fluoride")
                        .setDailyConsumed(nutationsMap.get("Fluoride").getDailyConsumed()
                                .add(food.getFluoride()));
                nutationsMap.get("Selenium")
                        .setDailyConsumed(nutationsMap.get("Selenium").getDailyConsumed()
                                .add(food.getSelenium()));
                nutationsMap.get("Chromium")
                        .setDailyConsumed(nutationsMap.get("Chromium").getDailyConsumed()
                                .add(food.getChromium()));
                nutationsMap.get("Molybdenum")
                        .setDailyConsumed(nutationsMap.get("Molybdenum").getDailyConsumed()
                                .add(food.getMolybdenum()));
                nutationsMap.get("Iodine")
                        .setDailyConsumed(nutationsMap.get("Iodine").getDailyConsumed()
                                .add(food.getIodine()));

                nutationsMap.get("Protein")
                        .setDailyConsumed(nutationsMap.get("Protein").getDailyConsumed()
                                .add(food.getProtein()));
                nutationsMap.get("Carbohydrates").setDailyConsumed(
                        nutationsMap.get("Carbohydrates").getDailyConsumed()
                                .add(food.getCarbohydrates()));
                nutationsMap.get("Fat").setDailyConsumed(
                        nutationsMap.get("Fat").getDailyConsumed().add(food.getFat()));

                nutationsMap.get("Fiber").setDailyConsumed(
                        nutationsMap.get("Fiber").getDailyConsumed().add(food.getFiber()));
                nutationsMap.get("Sugar").setDailyConsumed(
                        nutationsMap.get("Sugar").getDailyConsumed().add(food.getSugar()));
                nutationsMap.get("TransFat").setDailyConsumed(
                        nutationsMap.get("TransFat").getDailyConsumed()
                                .add(food.getTransFat()));
                nutationsMap.get("SaturatedFat").setDailyConsumed(
                        nutationsMap.get("SaturatedFat").getDailyConsumed()
                                .add(food.getSaturatedFat()));
                nutationsMap.get("PolyunsaturatedFat").setDailyConsumed(
                        nutationsMap.get("PolyunsaturatedFat").getDailyConsumed()
                                .add(food.getPolyunsaturatedFat()));
                nutationsMap.get("MonounsaturatedFat").setDailyConsumed(
                        nutationsMap.get("MonounsaturatedFat").getDailyConsumed()
                                .add(food.getMonounsaturatedFat()));

            }
            if (storage.getConsumedCalories() != null) {
                record.setDailyConsumedCalories(
                        record.getDailyConsumedCalories().add(storage.getConsumedCalories()));
            }

        }

    }

}
