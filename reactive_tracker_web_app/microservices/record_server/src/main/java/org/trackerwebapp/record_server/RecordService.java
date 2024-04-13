package org.trackerwebapp.record_server;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.trackerwebapp.record_server.domain.dto.CreateRecord;
import org.trackerwebapp.record_server.domain.dto.ModifyRecord;
import org.trackerwebapp.record_server.domain.dto.RecordView;
import org.trackerwebapp.record_server.domain.entity.RecordEntity;
import org.trackerwebapp.record_server.repository.RecordRepository;
import org.trackerwebapp.record_server.repository.UserDetailsRepository;
import org.trackerwebapp.record_server.utils.DailyCaloriesCalculator;
import org.trackerwebapp.record_server.utils.RecordModifier;
import org.trackerwebapp.record_server.utils.UserDetailsValidator;
import org.trackerwebapp.shared_interfaces.domain.exception.BadRequestException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RecordService {

  private final RecordRepository repository;
  private final UserDetailsRepository detailsRepository;

  public Flux<RecordView> getRecordsByUserId(String userId, BigDecimal moreThenDailyCalorie) {
    return repository
        .findAllByUserId(userId)
        .filter(data ->
            Optional.ofNullable(moreThenDailyCalorie)
                .map(calories -> data.getDailyCalories().compareTo(calories) > 0)
                .orElse(true)
        )
        .map(RecordView::toView);
  }

  public Mono<RecordView> getRecordByIdAndUserId(String recordId, String userId) {
    return getByIdAndUserId(recordId, userId)
        .map(RecordView::toView);
  }

  public Mono<RecordView> createRecord(CreateRecord dto, String userId) {
    return detailsRepository.findByIdAndUserId(dto.userDetailId(), userId)
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED)))
        .flatMap(UserDetailsValidator::validateUserDetails)
        .map(userDetails -> {
          RecordEntity entity = new RecordEntity();
          entity.setUserId(userId);
          entity.setDate(LocalDateTime.now());
          entity.setDailyCalories(DailyCaloriesCalculator.getCaloriesPerDay(userDetails));
          return entity;
        })
        .flatMap(entity -> RecordModifier.updateName(entity, dto))
        .flatMap(repository::save)
        .map(RecordView::toView);
  }

  public Mono<Void> deleteByIdAndUserId(String recordId, String userId) {
    return getByIdAndUserId(recordId, userId)
        .flatMap(record -> repository.deleteByIdAndUserId(record.getId(), record.getUserId()));
  }

  public Mono<RecordView> modifyRecord(String recordId, String userId, ModifyRecord dto) {
    return getByIdAndUserId(recordId, userId)
        .flatMap(entity -> RecordModifier.updateName(entity, dto))
        .flatMap(entity -> RecordModifier.updateDailyCalories(entity, dto))
        .flatMap(entity ->
            repository.updateRecordByIdAndUserId(
                    entity.getName(),
                    entity.getDailyCalories(),
                    entity.getId(),
                    entity.getUserId()
                )
                .then(Mono.just(entity))
        )
        .map(RecordView::toView);
  }

  private Mono<RecordEntity> getByIdAndUserId(String recordId, String userId) {
    return repository.findByIdAndUserId(recordId, userId)
        .switchIfEmpty(
            Mono.error(new BadRequestException("Record with id:" + recordId + " does not exist")));
  }
}
