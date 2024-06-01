package org.nutriGuideBuddy.utils.meals;

import org.nutriGuideBuddy.domain.dto.BadRequestException;
import org.nutriGuideBuddy.domain.entity.ServingEntity;
import org.nutriGuideBuddy.domain.dto.meal.ServingView;
import org.nutriGuideBuddy.domain.enums.ExceptionMessages;
import org.nutriGuideBuddy.utils.Validator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ServingModifier {

  public static Mono<ServingEntity> validateAndUpdateMainEntity(ServingView dto, String foodId) {
    return validateAndUpdateAmount(new ServingEntity(), dto)
        .flatMap(data -> validateAndUpdateWeight(data, dto))
        .flatMap(data -> validateAndUpdateMetric(data, dto))
        .map(data -> {
          data.setFoodId(foodId);
          data.setMain(true);
          return data;
        });
  }

  public static Mono<List<ServingEntity>> validateAndUpdateListOfEntities(List<ServingView> dto, String foodId) {
    if (dto == null ) {
      return Mono.error(new BadRequestException("alternative servings cannot be null"));
    }
    List<Mono<ServingEntity>> monoList = dto.stream()
        .map(view -> validateAndUpdateAmount(new ServingEntity(), view)
            .flatMap(data -> validateAndUpdateWeight(data, view))
            .flatMap(data -> validateAndUpdateMetric(data, view))
            .map(data -> {
              data.setFoodId(foodId);
              data.setMain(false);
              return data;
            }))
        .collect(Collectors.toList());

    return Flux.fromIterable(monoList)
        .flatMap(mono -> mono)
        .collectList();
  }

  private static Mono<ServingEntity> validateAndUpdateMetric(ServingEntity entity, ServingView dto) {
    return Mono.just(entity)
        .filter(u -> Validator.validateString(dto.metric(), 1 , 255))
        .flatMap(u -> {
          u.setMetric(dto.metric());
          return Mono.just(u);
        })
        .switchIfEmpty(Mono.error(
            new BadRequestException(ExceptionMessages.INVALID_STRING_LENGTH_MESSAGE.getMessage() + "for food metric."))
        );
  }

  private static Mono<ServingEntity> validateAndUpdateWeight(ServingEntity entity, ServingView dto) {
    return Mono.just(entity)
        .filter(u -> Validator.validateBigDecimal(dto.servingWeight(), BigDecimal.ZERO))
        .flatMap(u -> {
          u.setServingWeight(dto.servingWeight());
          return Mono.just(u);
        })
        .switchIfEmpty(Mono.error(
            new BadRequestException(ExceptionMessages.INVALID_NUMBER_MESSAGE.getMessage() +"for food size weight , must be greater then 0"))
        );
  }

  private static Mono<ServingEntity> validateAndUpdateAmount(ServingEntity entity, ServingView dto) {
    return Mono.just(entity)
        .filter(u -> Validator.validateBigDecimal(dto.amount(), BigDecimal.ZERO))
        .flatMap(u -> {
          u.setAmount(dto.amount());
          return Mono.just(u);
        })
        .switchIfEmpty(Mono.error(
            new BadRequestException(ExceptionMessages.INVALID_NUMBER_MESSAGE.getMessage() + "for food amount , must be greater then 0"))
        );
  }
}
