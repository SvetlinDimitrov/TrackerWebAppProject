package org.record.features.record.services;

import static org.record.infrastructure.exception.ExceptionMessages.INVALID_USER_TOKEN;
import static org.record.infrastructure.exception.ExceptionMessages.RECORD_NOT_FOUND;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.domain.record.dtos.RecordView;
import org.example.domain.user.dto.UserView;
import org.example.exceptions.throwable.BadRequestException;
import org.example.exceptions.throwable.NotFoundException;
import org.example.util.GsonWrapper;
import org.record.features.record.entity.Record;
import org.record.features.record.repository.RecordRepository;
import org.record.features.record.utils.RecordUtils;
import org.record.infrastructure.mappers.RecordMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecordServiceImp implements RecordService {

  private static final GsonWrapper GSON_WRAPPER = new GsonWrapper();
  private final RecordMapper recordMapper;
  private final RecordRepository repository;

  public List<RecordView> getAll(String userToken) {

    UserView user = getUser(userToken);

    return repository
        .findAllByUserId(user.id())
        .stream()
        .map(record -> recordMapper.toView(record, user))
        .collect(Collectors.toList());
  }

  public RecordView getById(String recordId, String userToken) {

    UserView user = getUser(userToken);
    Record record = getRecordByIdAndUserId(recordId, userToken);

    return recordMapper.toView(record, user);
  }

  public void create(String userToken, String name) {
    UserView user = getUser(userToken);

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

  private UserView getUser(String userToken) {
    try {
      return GSON_WRAPPER.fromJson(userToken, UserView.class);
    } catch (Exception e) {
      throw new BadRequestException(INVALID_USER_TOKEN);
    }
  }

  protected Record getRecordByIdAndUserId(String recordId, String userToken) {
    String userId = getUser(userToken).id();

    return repository.findByIdAndUserId(recordId, userId)
        .orElseThrow(() -> new NotFoundException(RECORD_NOT_FOUND));
  }
}
