package org.record;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.record.client.NutritionIntakeClient;
import org.record.client.StorageClient;
import org.record.client.dto.NutritionIntakeView;
import org.record.client.dto.RecordCreation;
import org.record.client.dto.StorageCreation;
import org.record.client.dto.StorageDeletion;
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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RecordServiceImp {

    private final RecordRepository recordRepository;
    private final NutritionIntakeClient nutritionIntakeClient;
    private final StorageClient storageClient;
    private final KafkaTemplate<String, RecordCreation> kafkaTemplateCreation;
    private final KafkaTemplate<String, Long> kafkaTemplateDeletion;
    private final KafkaTemplate<String, StorageCreation> kafkaTemplateStorageCreation;
    private final KafkaTemplate<String, StorageDeletion> kafkaTemplateStorageDeletion;
    private final Gson gson;

    public List<RecordView> getAllViewsByUserId(String userToken) throws UserNotFoundException {
        List<Record> records = recordRepository
                .findAllByUserId(getUserId(userToken).getId())
                .orElseThrow(() -> new UserNotFoundException(
                        getUserId(userToken).getUsername() + " does not have any records"));

        return records
                .stream()
                .map(record -> {
                    List<StorageView> allStorageWithRecordId = storageClient
                            .getAllStorageWithRecordId(record.getId());
                    List<NutritionIntakeView> allNutritionIntakesWithRecordId = nutritionIntakeClient
                            .getAllNutritionIntakesWithRecordId(record.getId());
                    return new RecordView(record.getId(),
                            allNutritionIntakesWithRecordId,
                            allStorageWithRecordId,
                            record.getDailyCalories(),
                            record.getUserId(),
                            String.valueOf(record.getDate()));
                })
                .collect(Collectors.toList());
    }

    public RecordView getViewByRecordIdAndUserId(Long recordId, String userToken) throws RecordNotFoundException {
        return recordRepository.findByIdAndUserId(recordId, getUserId(userToken).getId())
                .map(record -> {
                    List<StorageView> allStorageWithRecordId = storageClient
                            .getAllStorageWithRecordId(record.getId());
                    List<NutritionIntakeView> allNutritionIntakesWithRecordId = nutritionIntakeClient
                            .getAllNutritionIntakesWithRecordId(record.getId());
                    return new RecordView(record.getId(),
                            allNutritionIntakesWithRecordId,
                            allStorageWithRecordId,
                            record.getDailyCalories(),
                            record.getUserId(),
                            String.valueOf(record.getDate()));
                })
                .orElseThrow(() -> new RecordNotFoundException(
                        "Record with id " + recordId + " not found in user records."));
    }

    public Long addNewRecordByUserId(String userToken) throws RecordCreationException {
        UserView user = getUserId(userToken);

        RecordCreateDto recordCreateDto = RecordCreateDto
                .builder()
                .age(user.getAge())
                .height(new BigDecimal(user.getHeight()))
                .kilograms(new BigDecimal(user.getKilograms()))
                .gender(Gender.valueOf(user.getGender()))
                .workoutState(WorkoutState.valueOf(user.getWorkoutState()))
                .build();

        RecordValidator.validateRecord(recordCreateDto);

        Record record = new Record();
        record.setDate(LocalDate.now());

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

        return record.getId();
    }

    public void deleteById(Long recordId, String userToken) throws RecordNotFoundException {
        Record record = recordRepository.findByIdAndUserId(recordId, getUserId(userToken).getId())
                .orElseThrow(() -> new RecordNotFoundException(recordId.toString()));

        Message<Long> message = MessageBuilder
                .withPayload(recordId)
                .setHeader(KafkaHeaders.TOPIC, "record-deletion")
                .build();

        kafkaTemplateDeletion
                .send(message);

        recordRepository.delete(record);
    }

    private static BigDecimal getCaloriesPerDay(RecordCreateDto recordCreateDto, BigDecimal BMR) {
        return switch (recordCreateDto.workoutState) {
            case SEDENTARY -> BMR.multiply(new BigDecimal("1.2"));
            case LIGHTLY_ACTIVE -> BMR.multiply(new BigDecimal("1.375"));
            case MODERATELY_ACTIVE -> BMR.multiply(new BigDecimal("1.55"));
            case VERY_ACTIVE -> BMR.multiply(new BigDecimal("1.725"));
            case SUPER_ACTIVE -> BMR.multiply(new BigDecimal("1.9"));
        };
    }

    private static BigDecimal getBmr(RecordCreateDto recordCreateDto) {
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

    public void createStorage(StorageCreation storageCreation, String userToken) throws RecordNotFoundException {
        UserView user = getUserId(userToken);

        recordRepository.findByIdAndUserId(storageCreation.getRecordId(), user.getId())
                .orElseThrow(() -> new RecordNotFoundException(
                        storageCreation.getRecordId().toString()));

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

    private UserView getUserId(String userToken) {
        return gson.fromJson(userToken, UserView.class);
    }

}
