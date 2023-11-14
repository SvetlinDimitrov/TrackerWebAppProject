package org.record.services;

import java.math.BigDecimal;
import java.util.List;

import org.record.RecordRepository;
import org.record.client.dto.NutritionIntakeView;
import org.record.client.dto.StorageView;
import org.record.model.dtos.RecordCreateDto;
import org.record.model.dtos.RecordView;
import org.record.model.dtos.UserView;
import org.record.model.enums.Gender;
import org.springframework.stereotype.Service;
import org.record.model.entity.Record;
import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public abstract class AbstractRecordService {

    protected final Gson gson;
    protected final RecordRepository recordRepository;

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

    protected RecordView toRecordView(Record record , List<StorageView> allStorageWithRecordId, List<NutritionIntakeView> allNutritionIntakesWithRecordId){
        return RecordView.builder()
                .id(record.getId())
                .dailyIntakeViews(allNutritionIntakesWithRecordId)
                .storageViews(allStorageWithRecordId)
                .dailyConsumedCalories(record.getDailyConsumedCalories())
                .dailyCaloriesToConsume(record.getDailyCalories())
                .userID(record.getUserId())
                .date(String.valueOf(record.getDate()))
                .build();
    }
}
