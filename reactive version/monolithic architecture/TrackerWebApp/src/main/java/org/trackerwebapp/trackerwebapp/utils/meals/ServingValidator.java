package org.trackerwebapp.trackerwebapp.utils.meals;

import org.trackerwebapp.trackerwebapp.domain.dto.BadRequestException;
import org.trackerwebapp.trackerwebapp.domain.dto.meal.ServingView;
import org.trackerwebapp.trackerwebapp.domain.entity.CustomServingEntity;
import org.trackerwebapp.trackerwebapp.domain.entity.ServingEntity;
import org.trackerwebapp.trackerwebapp.utils.Validator;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class ServingValidator {

  public static Mono<ServingEntity> validateAndUpdateMetric(ServingEntity entity, ServingView dto) {
    return Mono.just(entity)
        .filter(u -> dto.metric() != null && !dto.metric().isBlank() && !dto.metric().trim().isEmpty())
        .flatMap(u -> {
          u.setMetric(dto.metric());
          return Mono.just(u);
        })
        .switchIfEmpty(Mono.error(
            new BadRequestException("Invalid food metric. Should be at least 1 char"))
        );
  }

  public static Mono<ServingEntity> validateAndUpdateWeight(ServingEntity entity, ServingView dto) {
    return Mono.just(entity)
        .filter(u -> Validator.validateBigDecimal(dto.servingWeight(), BigDecimal.ONE))
        .flatMap(u -> {
          u.setServingWeight(dto.servingWeight());
          return Mono.just(u);
        })
        .switchIfEmpty(Mono.error(
            new BadRequestException("Invalid food size weight , must be greater then 0"))
        );
  }

  public static Mono<ServingEntity> validateAndUpdateAmount(ServingEntity entity, ServingView dto) {
    return Mono.just(entity)
        .filter(u -> Validator.validateBigDecimal(dto.amount(), BigDecimal.ONE))
        .flatMap(u -> {
          u.setAmount(dto.amount());
          return Mono.just(u);
        })
        .switchIfEmpty(Mono.error(
            new BadRequestException("Invalid food amount , must be greater then 0"))
        );
  }

  public static Mono<CustomServingEntity> validateAndUpdateMetric(CustomServingEntity entity, ServingView dto) {
    return Mono.just(entity)
        .filter(u -> dto.metric() != null && !dto.metric().isBlank() && !dto.metric().trim().isEmpty())
        .flatMap(u -> {
          u.setMetric(dto.metric());
          return Mono.just(u);
        })
        .switchIfEmpty(Mono.error(
            new BadRequestException("Invalid food metric. Should be at least 1 char"))
        );
  }

  public static Mono<CustomServingEntity> validateAndUpdateWeight(CustomServingEntity entity, ServingView dto) {
    return Mono.just(entity)
        .filter(u -> Validator.validateBigDecimal(dto.servingWeight(), BigDecimal.ONE))
        .flatMap(u -> {
          u.setServingWeight(dto.servingWeight());
          return Mono.just(u);
        })
        .switchIfEmpty(Mono.error(
            new BadRequestException("Invalid food size weight , must be greater then 0"))
        );
  }

  public static Mono<CustomServingEntity> validateAndUpdateAmount(CustomServingEntity entity, ServingView dto) {
    return Mono.just(entity)
        .filter(u -> Validator.validateBigDecimal(dto.amount(), BigDecimal.ONE))
        .flatMap(u -> {
          u.setAmount(dto.amount());
          return Mono.just(u);
        })
        .switchIfEmpty(Mono.error(
            new BadRequestException("Invalid food amount , must be greater then 0"))
        );
  }
}
