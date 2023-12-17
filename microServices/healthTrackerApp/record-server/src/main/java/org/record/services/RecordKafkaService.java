package org.record.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.record.RecordRepository;
import org.record.client.dto.User;
import org.record.exceptions.InvalidJsonTokenException;
import org.record.exceptions.RecordCreationException;
import org.record.exceptions.RecordNotFoundException;
import org.record.exceptions.StorageException;
import org.record.model.entity.Record;
import org.record.utils.GsonWrapper;
import org.record.utils.RecordUtils;
import org.record.utils.RecordValidator;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecordKafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final RecordRepository recordRepository;
    private final GsonWrapper gson;

    @KafkaListener(topics = "USER_FIRST_CREATION", groupId = "group_user_creation_1", containerFactory = "kafkaListenerUserFirstCreation")
    public void recordFirstCreation(String userToken) throws RecordCreationException, InvalidJsonTokenException {
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

        String recordToken = gson.toJson(record);
        kafkaTemplate.send("RECORD_FIRST_CREATION", recordToken);
    }

    @Transactional
    @KafkaListener(topics = "USER_DELETION", groupId = "group_user_deletion_1", containerFactory = "kafkaListenerUserDeletion")
    public void deleteUser(String userToken) throws InvalidJsonTokenException {
        User user = getUserId(userToken);

        List<Record> records = recordRepository.findAllByUserId(user.getId());

        for (Record record : records) {
            kafkaTemplate.send("RECORD_DELETION", gson.toJson(record));
        }

        recordRepository.deleteAll(records);
    }

    public void addNewRecordByUserId(String userToken, String name)
            throws RecordCreationException, InvalidJsonTokenException {
        User user = getUserId(userToken);

        RecordValidator.validateRecord(user);

        Record record = new Record();

        record.setDate(LocalDate.now());

        if (name == null || name.isBlank()) {
            record.setName("Default" + RecordUtils.generateRandomNumbers(4));
        } else {
            record.setName(name);
        }

        BigDecimal BMR = RecordUtils.getBmr(user);
        BigDecimal caloriesPerDay = RecordUtils.getCaloriesPerDay(user, BMR);

        record.setDailyCalories(caloriesPerDay);
        record.setUserId(user.getId());

        recordRepository.saveAndFlush(record);

        String recordToken = gson.toJson(record);
        kafkaTemplate.send("RECORD_FIRST_CREATION", recordToken);
    }

    public void deleteById(Long recordId, String userToken)
            throws RecordNotFoundException, StorageException, InvalidJsonTokenException {
        Record record = getRecordByIdAndUserId(recordId, userToken);

        kafkaTemplate.send("RECORD_DELETION", gson.toJson(record));

        recordRepository.deleteById(record.getId());
    }

    private User getUserId(String userToken) throws InvalidJsonTokenException {
        try {
            return gson.fromJson(userToken, User.class);
        } catch (Exception e) {
            throw new InvalidJsonTokenException("Invalid user token.");
        }
    }

    private Record getRecordByIdAndUserId(Long recordId, String userToken)
            throws RecordNotFoundException, InvalidJsonTokenException {
        Long userId = getUserId(userToken).getId();

        return recordRepository.findByIdAndUserId(recordId, userId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "Record with id " + recordId + " not found in user records."));
    }
}
