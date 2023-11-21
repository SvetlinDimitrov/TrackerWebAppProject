package org.record.services;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.record.RecordRepository;
import org.record.client.dto.Food;
import org.record.client.dto.StorageView;
import org.record.exceptions.RecordNotFoundException;
import org.record.model.dtos.RecordCreateDto;
import org.record.model.dtos.RecordView;
import org.record.model.dtos.UserView;
import org.record.model.entity.NutritionIntake;
import org.record.model.entity.Record;
import org.record.model.enums.Gender;
import org.record.model.enums.WorkoutState;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public abstract class AbstractRecordService {

    protected final Gson gson;
    protected final RecordRepository recordRepository;
    protected final NutrientIntakeCreator nutrientIntakeCreator;

    protected UserView getUserId(String userToken) {
        return gson.fromJson(userToken, UserView.class);
    }

    protected BigDecimal getCaloriesPerDay(RecordCreateDto recordCreateDto, BigDecimal BMR) {
        return switch (recordCreateDto.workoutState) {
            case SEDENTARY -> BMR.multiply(new BigDecimal("1.2"));
            case LIGHTLY_ACTIVE -> BMR.multiply(new BigDecimal("1.375"));
            case MODERATELY_ACTIVE -> BMR.multiply(new BigDecimal("1.55"));
            case VERY_ACTIVE -> BMR.multiply(new BigDecimal("1.725"));
            case SUPER_ACTIVE -> BMR.multiply(new BigDecimal("1.9"));
        };
    }

    protected BigDecimal getBmr(RecordCreateDto recordCreateDto) {
        BigDecimal BMR;

        if (recordCreateDto.gender.equals(Gender.MALE)) {
            BMR = new BigDecimal("88.362")
                    .add(new BigDecimal("13.397").multiply(recordCreateDto.getKilograms()))
                    .add(new BigDecimal("4.799").multiply(recordCreateDto.getHeight()))
                    .subtract(new BigDecimal("5.677")
                            .add(new BigDecimal(recordCreateDto.getAge())));
        } else {
            BMR = new BigDecimal("447.593")
                    .add(new BigDecimal("9.247").multiply(recordCreateDto.getKilograms()))
                    .add(new BigDecimal("3.098").multiply(recordCreateDto.getHeight()))
                    .subtract(new BigDecimal("4.330")
                            .add(new BigDecimal(recordCreateDto.getAge())));
        }
        return BMR;
    }

    protected RecordView toRecordView(Record record, List<StorageView> storages, UserView user) {
        RecordView recordView = RecordView.builder()
                .id(record.getId())
                .storageViews(storages)
                .dailyConsumedCalories(null)
                .dailyCaloriesToConsume(record.getDailyCalories())
                .userID(record.getUserId())
                .date(String.valueOf(record.getDate()))
                .name(record.getName())
                .nutritionIntakesViews(null)
                .build();

        Map<String, NutritionIntake> nutationsMap = nutrientIntakeCreator.create(Gender.valueOf(user.getGender()),
                record.getDailyCalories(), WorkoutState.valueOf(user.getWorkoutState()));

        calculateNutrientsAndDailyConsumedCalories(recordView, nutationsMap);

        recordView.setNutritionIntakesViews(nutationsMap.values().stream().toList());

        return recordView;
    }

    protected Record getRecordByIdAndUserId(Long recordId, String userToken) throws RecordNotFoundException {
        Long userId = getUserId(userToken).getId();

        return recordRepository.findByIdAndUserId(recordId, userId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "Record with id " + recordId + " not found in user records."));
    }

    protected String generateRandomNumbers(int num) {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num; i++) {
            int randomNum = rand.nextInt(100);
            sb.append(randomNum);
        }
        return sb.toString();
    }

    private void calculateNutrientsAndDailyConsumedCalories(RecordView record,
            Map<String, NutritionIntake> nutationsMap) {

        for (StorageView storage : record.getStorageViews()) {

            for (Food food : storage.getFoods()) {
                nutationsMap.get("A").setDailyConsumed(nutationsMap.get("A").getDailyConsumed().add(food.getA()));
                nutationsMap.get("D").setDailyConsumed(nutationsMap.get("D").getDailyConsumed().add(food.getD()));
                nutationsMap.get("E").setDailyConsumed(nutationsMap.get("E").getDailyConsumed().add(food.getE()));
                nutationsMap.get("K").setDailyConsumed(nutationsMap.get("K").getDailyConsumed().add(food.getK()));
                nutationsMap.get("C").setDailyConsumed(nutationsMap.get("C").getDailyConsumed().add(food.getC()));
                nutationsMap.get("B1").setDailyConsumed(nutationsMap.get("B1").getDailyConsumed().add(food.getB1()));
                nutationsMap.get("B2").setDailyConsumed(nutationsMap.get("B2").getDailyConsumed().add(food.getB2()));
                nutationsMap.get("B3").setDailyConsumed(nutationsMap.get("B3").getDailyConsumed().add(food.getB3()));
                nutationsMap.get("B5").setDailyConsumed(nutationsMap.get("B5").getDailyConsumed().add(food.getB5()));
                nutationsMap.get("B6").setDailyConsumed(nutationsMap.get("B6").getDailyConsumed().add(food.getB6()));
                nutationsMap.get("B7").setDailyConsumed(nutationsMap.get("B7").getDailyConsumed().add(food.getB7()));
                nutationsMap.get("B9").setDailyConsumed(nutationsMap.get("B9").getDailyConsumed().add(food.getB9()));
                nutationsMap.get("B12").setDailyConsumed(nutationsMap.get("B12").getDailyConsumed().add(food.getB12()));

                nutationsMap.get("Calcium")
                        .setDailyConsumed(nutationsMap.get("Calcium").getDailyConsumed().add(food.getCalcium()));
                nutationsMap.get("Phosphorus")
                        .setDailyConsumed(nutationsMap.get("Phosphorus").getDailyConsumed().add(food.getPhosphorus()));
                nutationsMap.get("Magnesium")
                        .setDailyConsumed(nutationsMap.get("Magnesium").getDailyConsumed().add(food.getMagnesium()));
                nutationsMap.get("Sodium")
                        .setDailyConsumed(nutationsMap.get("Sodium").getDailyConsumed().add(food.getSodium()));
                nutationsMap.get("Potassium")
                        .setDailyConsumed(nutationsMap.get("Potassium").getDailyConsumed().add(food.getPotassium()));
                nutationsMap.get("Chloride")
                        .setDailyConsumed(nutationsMap.get("Chloride").getDailyConsumed().add(food.getChloride()));

                nutationsMap.get("Iron")
                        .setDailyConsumed(nutationsMap.get("Iron").getDailyConsumed().add(food.getIron()));
                nutationsMap.get("Zinc")
                        .setDailyConsumed(nutationsMap.get("Zinc").getDailyConsumed().add(food.getZinc()));
                nutationsMap.get("Copper")
                        .setDailyConsumed(nutationsMap.get("Copper").getDailyConsumed().add(food.getCopper()));
                nutationsMap.get("Manganese")
                        .setDailyConsumed(nutationsMap.get("Manganese").getDailyConsumed().add(food.getManganese()));
                nutationsMap.get("Fluoride")
                        .setDailyConsumed(nutationsMap.get("Fluoride").getDailyConsumed().add(food.getFluoride()));
                nutationsMap.get("Selenium")
                        .setDailyConsumed(nutationsMap.get("Selenium").getDailyConsumed().add(food.getSelenium()));
                nutationsMap.get("Chromium")
                        .setDailyConsumed(nutationsMap.get("Chromium").getDailyConsumed().add(food.getChromium()));
                nutationsMap.get("Molybdenum")
                        .setDailyConsumed(nutationsMap.get("Molybdenum").getDailyConsumed().add(food.getMolybdenum()));
                nutationsMap.get("Iodine")
                        .setDailyConsumed(nutationsMap.get("Iodine").getDailyConsumed().add(food.getIodine()));

                nutationsMap.get("Protein")
                        .setDailyConsumed(nutationsMap.get("Protein").getDailyConsumed().add(food.getProtein()));
                nutationsMap.get("Carbohydrates").setDailyConsumed(
                        nutationsMap.get("Carbohydrates").getDailyConsumed().add(food.getCarbohydrates()));
                nutationsMap.get("Fat").setDailyConsumed(nutationsMap.get("Fat").getDailyConsumed().add(food.getFat()));
            }
            record.setDailyCaloriesToConsume(record.getDailyCaloriesToConsume().add(storage.getConsumedCalories()));
        }

    }

}
