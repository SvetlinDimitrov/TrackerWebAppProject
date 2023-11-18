package org.record.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.record.RecordRepository;
import org.record.client.NutritionIntakeClient;
import org.record.client.StorageClient;
import org.record.client.dto.NutritionIntakeView;
import org.record.client.dto.StorageView;
import org.record.exceptions.RecordCreationException;
import org.record.exceptions.RecordNotFoundException;
import org.record.exceptions.UserNotFoundException;
import org.record.model.dtos.RecordCreateDto;
import org.record.model.dtos.RecordView;
import org.record.model.dtos.UserView;
import org.record.model.entity.Record;
import org.record.model.enums.Gender;
import org.record.model.enums.WorkoutState;
import org.record.utils.RecordValidator;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service
public class RecordServiceImp extends AbstractRecordService {

    private final NutritionIntakeClient nutritionIntakeClient;
    private final StorageClient storageClient;

    public RecordServiceImp(Gson gson,
            RecordRepository recordRepository,
            NutritionIntakeClient nutritionIntakeClient,
            StorageClient storageClient) {
        super(gson, recordRepository);
        this.nutritionIntakeClient = nutritionIntakeClient;
        this.storageClient = storageClient;
    }

    public List<RecordView> getAllViewsByUserId(String userToken) throws UserNotFoundException {
        List<Record> records = recordRepository
                .findAllByUserId(getUserId(userToken).getId())
                .orElseThrow(() -> new UserNotFoundException(
                        getUserId(userToken).getUsername() + " does not have any records"));

        return records
                .stream()
                .map(record -> {
                    List<StorageView> allStorageWithRecordId = storageClient
                            .getAllStorageWithRecordId(record.getId(), userToken);
                    List<NutritionIntakeView> allNutritionIntakesWithRecordId = nutritionIntakeClient
                            .getAllNutritionIntakesWithRecordId(record.getId(), userToken);
                    return toRecordView(record, allStorageWithRecordId, allNutritionIntakesWithRecordId);
                })
                .collect(Collectors.toList());
    }

    public RecordView getViewByRecordIdAndUserId(Long recordId, String userToken) throws RecordNotFoundException {
        return recordRepository.findByIdAndUserId(recordId, getUserId(userToken).getId())
                .map(record -> {
                    List<StorageView> allStorageWithRecordId = storageClient
                            .getAllStorageWithRecordId(record.getId(), userToken);
                    List<NutritionIntakeView> allNutritionIntakesWithRecordId = nutritionIntakeClient
                            .getAllNutritionIntakesWithRecordId(record.getId(), userToken);
                    return toRecordView(record, allStorageWithRecordId, allNutritionIntakesWithRecordId);
                })
                .orElseThrow(() -> new RecordNotFoundException(
                        "Record with id " + recordId + " not found in user records."));
    }

    public RecordView addNewRecordByUserId(String userToken, String name) throws RecordCreationException {
        UserView user = getUserId(userToken);

        RecordValidator.validateRecord(user);

        RecordCreateDto recordCreateDto = RecordCreateDto
                .builder()
                .age(user.getAge())
                .height(new BigDecimal(user.getHeight()))
                .kilograms(new BigDecimal(user.getKilograms()))
                .gender(Gender.valueOf(user.getGender()))
                .workoutState(WorkoutState.valueOf(user.getWorkoutState()))
                .build();

        Record record = new Record();
        record.setDate(LocalDate.now());

        if (name == null || name.isBlank()) {
            record.setName("Default" + generateRandomNumbers(4));
        } else {
            record.setName(name);
        }

        BigDecimal BMR = getBmr(recordCreateDto);
        BigDecimal caloriesPerDay = getCaloriesPerDay(recordCreateDto, BMR);

        record.setDailyCalories(caloriesPerDay);
        record.setUserId(user.getId());

        recordRepository.saveAndFlush(record);

        List<NutritionIntakeView> nutritionIntakeViews = nutritionIntakeClient.createNutritionIntake(record.getId(),
                Gender.valueOf(user.getGender()),
                record.getDailyCalories(),
                WorkoutState.valueOf(user.getWorkoutState()) , userToken).getBody();

        List<StorageView> storageViews = storageClient.firstCreationOfRecord(record.getId() , userToken).getBody();

        return toRecordView(record, storageViews, nutritionIntakeViews);
    }
}
