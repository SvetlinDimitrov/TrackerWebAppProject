package org.record.services;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.record.RecordRepository;
import org.record.client.dto.FoodView;
import org.record.client.dto.RecordCreation;
import org.record.client.dto.StorageCreation;
import org.record.client.dto.StorageDeletion;
import org.record.exceptions.RecordCreationException;
import org.record.exceptions.RecordNotFoundException;
import org.record.model.dtos.RecordCreateDto;
import org.record.model.dtos.UserView;
import org.record.model.entity.Record;
import org.record.model.enums.Gender;
import org.record.model.enums.WorkoutState;
import org.record.utils.RecordValidator;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

@Service
public class RecordKafkaService extends AbstractRecordService {

    private final KafkaTemplate<String, RecordCreation> kafkaTemplateCreation;
    private final KafkaTemplate<String, Long> kafkaTemplateDeletion;
    private final KafkaTemplate<String, StorageCreation> kafkaTemplateStorageCreation;
    private final KafkaTemplate<String, StorageDeletion> kafkaTemplateStorageDeletion;

    public RecordKafkaService(
            Gson gson,
            RecordRepository recordRepository,
            KafkaTemplate<String, RecordCreation> kafkaTemplateCreation,
            KafkaTemplate<String, Long> kafkaTemplateDeletion,
            KafkaTemplate<String, StorageCreation> kafkaTemplateStorageCreation,
            KafkaTemplate<String, StorageDeletion> kafkaTemplateStorageDeletion) {
        super(gson, recordRepository);
        this.kafkaTemplateCreation = kafkaTemplateCreation;
        this.kafkaTemplateDeletion = kafkaTemplateDeletion;
        this.kafkaTemplateStorageCreation = kafkaTemplateStorageCreation;
        this.kafkaTemplateStorageDeletion = kafkaTemplateStorageDeletion;
    }

    @KafkaListener(topics = "user-first-creation", groupId = "group_user_creation_1", containerFactory = "kafkaListenerUserFirstCreation")
    public void addNewRecordByUserId(String userToken , String name) throws RecordCreationException {
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

        if(name == null || name == userToken || name.isBlank()){
            record.setName("Default" + generateRandomNumbers(4));
        }else{
            record.setName(name);
        }

        BigDecimal BMR = getBmr(recordCreateDto);
        BigDecimal caloriesPerDay = getCaloriesPerDay(recordCreateDto, BMR);

        record.setDailyCalories(caloriesPerDay);
        record.setUserId(user.getId());

        recordRepository.saveAndFlush(record);

        RecordCreation intakeCreateDto = new RecordCreation(
                record.getId(),
                Gender.valueOf(user.getGender()),
                record.getDailyCalories(),
                WorkoutState.valueOf(user.getWorkoutState()));

        Message<RecordCreation> message = MessageBuilder
                .withPayload(intakeCreateDto)
                .setHeader(KafkaHeaders.TOPIC, "record-creation")
                .build();

        kafkaTemplateCreation
                .send(message);
    }

    public void deleteById(Long recordId, String userToken) throws RecordNotFoundException {
        Record record = recordRepository.findByIdAndUserId(recordId, getUserId(userToken).getId())
                .orElseThrow(() -> new RecordNotFoundException(
                        "Record with id " + recordId + " not found in user records."));

        Message<Long> message = MessageBuilder
                .withPayload(recordId)
                .setHeader(KafkaHeaders.TOPIC, "record-deletion")
                .build();

        kafkaTemplateDeletion
                .send(message);

        recordRepository.delete(record);
    }

    public void createStorage(StorageCreation storageCreation, String userToken) throws RecordNotFoundException {
        UserView user = getUserId(userToken);

        recordRepository.findByIdAndUserId(storageCreation.getRecordId(), user.getId())
                .orElseThrow(() -> new RecordNotFoundException("Record with id "
                        + storageCreation.getRecordId() + " not found in user records."));

        Message<StorageCreation> message = MessageBuilder
                .withPayload(storageCreation)
                .setHeader(KafkaHeaders.TOPIC, "storage-creation")
                .build();

        kafkaTemplateStorageCreation.send(message);
    }

    public void deleteStorage(StorageDeletion storageDeletion, String userToken) throws RecordNotFoundException {
        UserView user = getUserId(userToken);

        recordRepository.findByIdAndUserId(storageDeletion.getRecordId(), user.getId())
                .orElseThrow(() -> new RecordNotFoundException(
                        storageDeletion.getRecordId().toString()));

        Message<StorageDeletion> message = MessageBuilder
                .withPayload(storageDeletion)
                .setHeader(KafkaHeaders.TOPIC, "storage-deletion")
                .build();

        kafkaTemplateStorageDeletion.send(message);
    }

    @KafkaListener(topics = "storage-filling", groupId = "group_storage_filling_removing_2", containerFactory = "kafkaListenerStorageFillingRemoving")
    @Transactional
    public void changeNutritionIntakeFilling(FoodView foodView) throws RecordNotFoundException {

        Record record = recordRepository.findById(foodView.getRecordId())
                .orElseThrow(() -> new RecordNotFoundException("Record with id "
                        + foodView.getRecordId() + " not found in user records."));

        record.setDailyConsumedCalories(record.getDailyConsumedCalories().add(foodView.getCalories()));
        recordRepository.saveAndFlush(record);
    }

    @KafkaListener(topics = "storage-removing", groupId = "group_storage_filling_removing_2", containerFactory = "kafkaListenerStorageFillingRemoving")
    @Transactional
    public void changeNutritionIntakeRemoving(FoodView foodView) throws RecordNotFoundException {

        Record record = recordRepository.findById(foodView.getRecordId())
                .orElseThrow(() -> new RecordNotFoundException("Record with id "
                        + foodView.getRecordId() + " not found in user records."));

        record.setDailyConsumedCalories(record.getDailyConsumedCalories().subtract(foodView.getCalories()));
        recordRepository.saveAndFlush(record);
    }
}
