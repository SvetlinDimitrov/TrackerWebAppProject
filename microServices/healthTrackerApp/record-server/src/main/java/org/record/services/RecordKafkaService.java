package org.record.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.record.RecordRepository;
import org.record.client.NutritionIntakeClient;
import org.record.client.StorageClient;
import org.record.client.dto.FoodView;
import org.record.client.dto.NutritionIntakeView;
import org.record.client.dto.StorageCreation;
import org.record.client.dto.StorageDeletion;
import org.record.client.dto.StorageView;
import org.record.exceptions.RecordCreationException;
import org.record.exceptions.RecordNotFoundException;
import org.record.model.dtos.RecordCreateDto;
import org.record.model.dtos.UserView;
import org.record.model.entity.Record;
import org.record.model.enums.Gender;
import org.record.model.enums.KafkaProducerTopics;
import org.record.model.enums.WorkoutState;
import org.record.utils.RecordValidator;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

@Service
public class RecordKafkaService extends AbstractRecordService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final NutritionIntakeClient nutritionIntakeClient;;
    private final StorageClient storageClient;

    public RecordKafkaService(
            Gson gson,
            RecordRepository recordRepository,
            KafkaTemplate<String, String> kafkaTemplate,
            NutritionIntakeClient nutritionIntakeClient,
            StorageClient storageClient) {
        super(gson, recordRepository);
        this.kafkaTemplate = kafkaTemplate;
        this.nutritionIntakeClient = nutritionIntakeClient;
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

        nutritionIntakeClient.createNutritionIntake(record.getId(),
                Gender.valueOf(user.getGender()),
                record.getDailyCalories(),
                WorkoutState.valueOf(user.getWorkoutState()), userToken);

        storageClient.firstCreationOfRecord(record.getId(), userToken);
    }

    public void deleteById(Long recordId, String userToken) throws RecordNotFoundException {
        Record record = recordRepository.findByIdAndUserId(recordId, getUserId(userToken).getId())
                .orElseThrow(() -> new RecordNotFoundException(
                        "Record with id " + recordId + " not found in user records."));

        kafkaTemplate.send(KafkaProducerTopics.RECORD_DELETION.name(), gson.toJson(recordId));

        recordRepository.delete(record);
    }

    public void createStorage(StorageCreation storageCreation, String userToken) throws RecordNotFoundException {
        UserView user = getUserId(userToken);

        recordRepository.findByIdAndUserId(storageCreation.getRecordId(), user.getId())
                .orElseThrow(() -> new RecordNotFoundException("Record with id "
                        + storageCreation.getRecordId() + " not found in user records."));

        kafkaTemplate.send(KafkaProducerTopics.STORAGE_CREATION.name(), gson.toJson(storageCreation));
    }

    public void deleteStorage(StorageDeletion storageDeletion, String userToken) throws RecordNotFoundException {
        UserView user = getUserId(userToken);

        recordRepository.findByIdAndUserId(storageDeletion.getRecordId(), user.getId())
                .orElseThrow(() -> new RecordNotFoundException(
                        storageDeletion.getRecordId().toString()));

        kafkaTemplate.send(KafkaProducerTopics.STORAGE_DELETION.name(), gson.toJson(storageDeletion));
    }

    @KafkaListener(topics = "STORAGE_FILLING", groupId = "group_storage_filling_removing_2", containerFactory = "kafkaListenerStorageFillingRemoving")
    @Transactional
    public void changeNutritionIntakeFilling(String jsonToken) throws RecordNotFoundException {

        FoodView foodView = gson.fromJson(jsonToken, FoodView.class);

        Record record = recordRepository.findById(foodView.getRecordId())
                .orElseThrow(() -> new RecordNotFoundException("Record with id "
                        + foodView.getRecordId() + " not found in user records."));

        record.setDailyConsumedCalories(record.getDailyConsumedCalories().add(foodView.getCalories()));
        recordRepository.saveAndFlush(record);
    }

    @KafkaListener(topics = "STORAGE_REMOVING", groupId = "group_storage_filling_removing_2", containerFactory = "kafkaListenerStorageFillingRemoving")
    @Transactional
    public void changeNutritionIntakeRemoving(String jsonToken) throws RecordNotFoundException {

        FoodView foodView = gson.fromJson(jsonToken, FoodView.class);

        Record record = recordRepository.findById(foodView.getRecordId())
                .orElseThrow(() -> new RecordNotFoundException("Record with id "
                        + foodView.getRecordId() + " not found in user records."));

        record.setDailyConsumedCalories(record.getDailyConsumedCalories().subtract(foodView.getCalories()));
        recordRepository.saveAndFlush(record);
    }
}
