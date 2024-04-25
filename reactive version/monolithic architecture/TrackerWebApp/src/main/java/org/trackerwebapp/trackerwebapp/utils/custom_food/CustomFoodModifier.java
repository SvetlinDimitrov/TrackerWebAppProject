package org.trackerwebapp.trackerwebapp.utils.custom_food;

import org.trackerwebapp.trackerwebapp.domain.dto.BadRequestException;
import org.trackerwebapp.trackerwebapp.domain.dto.custom_food.CustomInsertFoodDto;
import org.trackerwebapp.trackerwebapp.domain.entity.CustomFoodEntity;
import org.trackerwebapp.trackerwebapp.domain.enums.AllowedFoodUnits;
import org.trackerwebapp.trackerwebapp.utils.Validator;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class CustomFoodModifier {

  public static Mono<CustomFoodEntity> validateAndUpdateName(CustomFoodEntity entity, CustomInsertFoodDto dto) {
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

  public static Mono<CustomFoodEntity> validateAndUpdateMeasurement(CustomFoodEntity entity, CustomInsertFoodDto dto) {
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

  public static Mono<CustomFoodEntity> validateAndUpdateSize(CustomFoodEntity entity, CustomInsertFoodDto dto) {
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
