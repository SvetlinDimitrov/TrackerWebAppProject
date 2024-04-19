package org.trackerwebapp.meal_server.utils;

import java.math.BigDecimal;
import org.trackerwebapp.meal_server.domain.dto.InsertFoodDto;
import org.trackerwebapp.meal_server.domain.entity.FoodEntity;
import org.trackerwebapp.shared_interfaces.domain.enums.AllowedFoodUnits;
import org.trackerwebapp.shared_interfaces.domain.exception.BadRequestException;
import org.trackerwebapp.shared_interfaces.domain.utils.Validator;
import reactor.core.publisher.Mono;

public class FoodModifier {

  public static Mono<FoodEntity> validateAndUpdateName(FoodEntity entity, InsertFoodDto dto) {
    return Mono.just(entity)
        .filter(u -> Validator.validateString(dto.name(), 2))
        .flatMap(u -> {
          u.setName(dto.name());
          return Mono.just(u);
        })
        .switchIfEmpty(Mono.error(
            new BadRequestException("Invalid food name length , must be greater then 2 chars"))
        );
  }

  public static Mono<FoodEntity> validateAndUpdateMeasurement(FoodEntity entity,
      InsertFoodDto dto) {
    return Mono.just(entity)
        .filter(u -> dto.measurement() != null && AllowedFoodUnits.GRAM.getSymbol()
            .equals(dto.measurement()))
        .flatMap(u -> {
          u.setMeasurement(dto.measurement());
          return Mono.just(u);
        })
        .switchIfEmpty(Mono.error(
            new BadRequestException("Invalid food measurement"))
        );
  }

  public static Mono<FoodEntity> validateAndUpdateSize(FoodEntity entity, InsertFoodDto dto) {
    return Mono.just(entity)
        .filter(u -> Validator.validateBigDecimal(dto.size(), BigDecimal.ONE))
        .flatMap(u -> {
          u.setSize(dto.size());
          return Mono.just(u);
        })
        .switchIfEmpty(Mono.error(
            new BadRequestException("Invalid food size , must be greater then 0"))
        );
  }
}
