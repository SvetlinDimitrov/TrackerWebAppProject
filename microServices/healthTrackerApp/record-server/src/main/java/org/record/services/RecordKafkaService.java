package org.record.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.record.RecordRepository;
import org.record.client.StorageClient;
import org.record.client.dto.User;
import org.record.exceptions.RecordCreationException;
import org.record.model.entity.Record;
import org.record.utils.GsonWrapper;
import org.record.utils.NutrientIntakeCreator;
import org.record.utils.RecordUtils;
import org.record.utils.RecordValidator;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class RecordKafkaService extends AbstractRecordService {

    private final StorageClient storageClient;

    public RecordKafkaService(
            RecordRepository recordRepository,
            NutrientIntakeCreator nutrientIntakeCreator,
            StorageClient storageClient,
            GsonWrapper gsonWrapper) {
        super(gsonWrapper, recordRepository, nutrientIntakeCreator);
        this.storageClient = storageClient;
    }

    @KafkaListener(topics = "USER_FIRST_CREATION", groupId = "group_user_creation_1", containerFactory = "kafkaListenerUserFirstCreation")
    public void addNewRecordByUserId(String userToken) throws RecordCreationException {
        User user = getUserId(userToken);

        RecordValidator.validateRecord(user);

        Record record = new Record();
        record.setDate(LocalDate.now());

        record.setName("Default" + RecordUtils.generateRandomNumbers(4));

        BigDecimal BMR = RecordUtils.getBmr(user);
        BigDecimal caloriesPerDay = RecordUtils.getCaloriesPerDay(user, BMR);

        record.setDailyCalories(caloriesPerDay);
        record.setUserId(user.getId());

        recordRepository.saveAndFlush(record);

        storageClient.createStorageFirstCreation(record.getId(), userToken);
    }
    @Transactional
    @KafkaListener(topics = "USER_DELETION", groupId = "group_user_deletion_1", containerFactory = "kafkaListenerUserDeletion")
    public void deleteUser(String userToken){
        User user = getUserId(userToken);

        List<Record> records = recordRepository.findAllByUserId(user.getId());

        for (Record record : records) {
            storageClient.deleteAllStoragesByRecordId(record.getId(), userToken);
        }

        recordRepository.deleteAll(records);
    }

}
