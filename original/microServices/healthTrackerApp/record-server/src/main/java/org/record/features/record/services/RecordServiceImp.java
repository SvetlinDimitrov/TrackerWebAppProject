package org.record.features.record.services;

import static org.record.infrastructure.exception.ExceptionMessages.RECORD_NOT_FOUND;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.domain.record.dtos.RecordView;
import org.example.domain.user.dto.UserView;
import org.example.exceptions.throwable.NotFoundException;
import org.example.util.UserExtractor;
import org.record.features.record.entity.Record;
import org.record.features.record.repository.RecordRepository;
import org.record.features.record.utils.RecordUtils;
import org.record.infrastructure.mappers.RecordMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecordServiceImp implements RecordService {

  private final RecordMapper recordMapper;
  private final RecordRepository repository;

  public List<RecordView> getAll(String userToken) {

    UserView user = UserExtractor.get(userToken);

    return repository
        .findAllByUserId(user.id())
        .stream()
        .map(record -> recordMapper.toView(record, user))
        .collect(Collectors.toList());
  }

  public RecordView getById(String recordId, String userToken) {

    UserView user = UserExtractor.get(userToken);
    Record record = getRecordByIdAndUserId(recordId, userToken);

    return recordMapper.toView(record, user);
  }

  public void create(String userToken, String name) {
    UserView user = UserExtractor.get(userToken);

    Record record = new Record();

    record.setDate(LocalDateTime.now());

    if (name == null || name.isBlank()) {
      record.setName("Default" + RecordUtils.generateRandomNumbers(4));
    } else {
      record.setName(name);
    }

    BigDecimal BMR = RecordUtils.getBmr(user);
    BigDecimal caloriesPerDay = RecordUtils.getCaloriesPerDay(user, BMR);

    record.setDailyCalories(caloriesPerDay);
    record.setUserId(user.id());

    repository.save(record);
  }

  public void delete(String recordId, String userToken) {
    Record record = getRecordByIdAndUserId(recordId, userToken);

    repository.deleteById(record.getId());
  }

  public Record getRecordByIdAndUserId(String recordId, String userToken) {
    UUID userId = UserExtractor.get(userToken).id();

    return repository.findByIdAndUserId(recordId, userId)
        .orElseThrow(() -> new NotFoundException(RECORD_NOT_FOUND));
  }
}
