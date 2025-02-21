package org.record.infrastructure.kafka.service;

import static org.record.infrastructure.exception.ExceptionMessages.INVALID_USER_TOKEN;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.domain.user.dto.UserView;
import org.example.exceptions.throwable.BadRequestException;
import org.example.util.GsonWrapper;
import org.record.features.record.entity.Record;
import org.record.features.record.repository.RecordRepository;
import org.record.features.record.utils.RecordUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecordKafkaService {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final RecordRepository recordRepository;
  private static final GsonWrapper GSON_WRAPPER = new GsonWrapper();

  @KafkaListener(topics = "USER_FIRST_CREATION", groupId = "group_user_creation_1", containerFactory = "kafkaListenerUserFirstCreation")
  public void recordFirstCreation(String userToken) {
    UserView user = getUserId(userToken);

    Record record = new Record();
    record.setDate(LocalDateTime.now());

    record.setName("Default" + RecordUtils.generateRandomNumbers(4));

    BigDecimal BMR = RecordUtils.getBmr(user);
    BigDecimal caloriesPerDay = RecordUtils.getCaloriesPerDay(user, BMR);

    record.setDailyCalories(caloriesPerDay);
    record.setUserId(user.id());

    recordRepository.save(record);

    String recordToken = GSON_WRAPPER.toJson(record);
    kafkaTemplate.send("RECORD_FIRST_CREATION", recordToken);
  }

  @KafkaListener(topics = "USER_DELETION", groupId = "group_user_deletion_1", containerFactory = "kafkaListenerUserDeletion")
  public void deleteUser(String userToken) {
    UserView user = getUserId(userToken);

    List<Record> records = recordRepository.findAllByUserId(user.id());

    recordRepository.deleteAll(records);
  }

  private UserView getUserId(String userToken) {
    try {
      return GSON_WRAPPER.fromJson(userToken, UserView.class);
    } catch (Exception e) {
      throw new BadRequestException(INVALID_USER_TOKEN);
    }
  }
}
