package org.trackerwebapp.trackerwebapp.utils.meals;

import org.trackerwebapp.trackerwebapp.domain.dto.BadRequestException;
import org.trackerwebapp.trackerwebapp.domain.dto.meal.InsertFoodDto;
import org.trackerwebapp.trackerwebapp.domain.entity.FoodEntity;
import org.trackerwebapp.trackerwebapp.utils.Validator;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.trackerwebapp.trackerwebapp.domain.enums.ExceptionMessages.INVALID_STRING_LENGTH_MESSAGE;

public class FoodModifier {

  public static Mono<FoodEntity> validateAndUpdateEntity (FoodEntity entity , InsertFoodDto dto) {
    return validateAndUpdateName(entity , dto);
  }

  private static Mono<FoodEntity> validateAndUpdateName(FoodEntity entity, InsertFoodDto dto) {
    return Mono.just(entity)
        .filter(u -> Validator.validateString(dto.name(), 1 , 255))
        .flatMap(u -> {
          u.setName(dto.name());
          return Mono.just(u);
        })
        .switchIfEmpty(Mono.error(
            new BadRequestException(INVALID_STRING_LENGTH_MESSAGE + " for food name"))
        );
  }
}
