package org.trackerwebapp.record_server.utils;

import java.math.BigDecimal;
import org.trackerwebapp.record_server.domain.dto.CreateRecord;
import org.trackerwebapp.record_server.domain.dto.ModifyRecord;
import org.trackerwebapp.record_server.domain.entity.RecordEntity;
import org.trackerwebapp.shared_interfaces.domain.exception.BadRequestException;
import reactor.core.publisher.Mono;

public class RecordModifier {

  public static Mono<RecordEntity> updateName(RecordEntity entity, CreateRecord dto) {
    return modifyName(entity, dto.name());
  }

  public static Mono<RecordEntity> updateName(RecordEntity entity, ModifyRecord dto) {
    return modifyName(entity, dto.name());
  }

  public static Mono<RecordEntity> updateDailyCalories(RecordEntity entity, ModifyRecord dto) {
    return Mono.just(entity)
        .filter(data -> dto.dailyCalories() != null)
        .flatMap(data -> {
          if (dto.dailyCalories().compareTo(BigDecimal.ZERO) > 0) {
            data.setDailyCalories(dto.dailyCalories());
            return Mono.just(data);
          } else {
            return Mono.error(new BadRequestException("Invalid daily calories value. Must be greater then zero"));
          }
        })
        .switchIfEmpty(Mono.just(entity));
  }

  private static Mono<RecordEntity> modifyName(RecordEntity entity, String name) {
    return Mono.just(entity)
        .filter(data -> name != null && !name.isBlank())
        .flatMap(data -> {
          if (name.trim().length() >= 2) {
            data.setName(name);
            return Mono.just(data);
          } else {
            return Mono.error(new BadRequestException("Invalid name length"));
          }
        })
        .switchIfEmpty(Mono.just(entity));
  }
}
