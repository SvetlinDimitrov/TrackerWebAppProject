package org.record.services;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.record.RecordRepository;
import org.record.client.StorageClient;
import org.record.exceptions.RecordCreationException;
import org.record.model.dtos.RecordCreateDto;
import org.record.model.dtos.UserView;
import org.record.model.entity.Record;
import org.record.model.enums.Gender;
import org.record.model.enums.WorkoutState;
import org.record.utils.RecordValidator;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service
public class RecordKafkaService extends AbstractRecordService {

    private final StorageClient storageClient;

    public RecordKafkaService(
            Gson gson,
            RecordRepository recordRepository,
            NutrientIntakeCreator nutrientIntakeCreator,
            StorageClient storageClient) {
        super(gson, recordRepository, nutrientIntakeCreator);
        this.storageClient = storageClient;
    }

    @KafkaListener(topics = "USER_FIRST_CREATION", groupId = "group_user_creation_1", containerFactory = "kafkaListenerUserFirstCreation")
    public void addNewRecordByUserId(String userToken) throws RecordCreationException {
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

        record.setName("Default" + generateRandomNumbers(4));

        BigDecimal BMR = getBmr(recordCreateDto);
        BigDecimal caloriesPerDay = getCaloriesPerDay(recordCreateDto, BMR);

        record.setDailyCalories(caloriesPerDay);
        record.setUserId(user.getId());

        recordRepository.saveAndFlush(record);

        storageClient.createStorageFirstCreation(record.getId());
    }

}
