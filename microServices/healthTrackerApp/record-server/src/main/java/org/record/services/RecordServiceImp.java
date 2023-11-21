package org.record.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.record.RecordRepository;
import org.record.client.StorageClient;
import org.record.exceptions.FoodException;
import org.record.exceptions.RecordCreationException;
import org.record.exceptions.RecordNotFoundException;
import org.record.exceptions.StorageAlreadyExistException;
import org.record.exceptions.StorageNotFoundException;
import org.record.exceptions.UserNotFoundException;
import org.record.model.dtos.RecordCreateDto;
import org.record.model.dtos.RecordView;
import org.record.model.dtos.UserView;

import org.record.model.entity.NutritionIntake;
import org.record.model.entity.Record;

import org.record.model.enums.Gender;
import org.record.model.enums.WorkoutState;
import org.record.utils.RecordValidator;
import org.springframework.stereotype.Service;
i

import com.google.gson.Gson;

import feign.FeignException;

@Service
public class RecordServiceImp extends AbstractRecordService {

    private final StorageClient storageClient;;

    public RecordServiceImp(
            Gson gson,
            RecordRepository recordRepository,
            NutrientIntakeCreator nutrientIntakeCreator,
            StorageClient storageClient) {
        super(gson, recordRepository, nutrientIntakeCreator);
        this.storageClient = storageClient;
    }

    public List<RecordView> getAllViewsByUserId(String userToken) throws UserNotFoundException {

        List<Record> records = recordRepository
                .findAllByUserId(getUserId(userToken).getId())
                .orElseThrow(() -> new UserNotFoundException(
                        getUserId(userToken).getUsername() + " does not have any records"));

        UserView user = getUserId(userToken);

        return records
                .stream()
                .map(record -> toRecordView(record , storageClient.getAllStorages(record.getId()).getBody() , user))
                .collect(Collectors.toList());
    }

    public RecordView getViewByRecordIdAndUserId(Long recordId, String userToken) throws RecordNotFoundException {

        Record record = getRecordByIdAndUserId(recordId, userToken);

        return toRecordView(record , storageClient.getAllStorages(record.getId()).getBody() , getUserId(userToken));
    }

    public void addNewRecordByUserId(String userToken, String name) throws RecordCreationException {
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

        storageClient.createStorageFirstCreation(record.getId());
    }

    public void deleteById(Long recordId, String userToken) {
        Record record = getRecordByIdAndUserId(recordId, userToken);

        storageClient.deleteAllStoragesByRecordId(record.getId());
       
        recordRepository.deleteById(record.getId());
    }

}
