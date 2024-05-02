package org.nutriGuideBuddy.utils.meals;

import org.nutriGuideBuddy.domain.dto.BadRequestException;
import org.nutriGuideBuddy.domain.entity.CalorieEntity;
import org.nutriGuideBuddy.domain.dto.meal.CalorieView;
import org.nutriGuideBuddy.domain.enums.AllowedCalorieUnits;
import org.nutriGuideBuddy.domain.enums.ExceptionMessages;
import org.nutriGuideBuddy.utils.Validator;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class CalorieModifier {

  public static Mono<CalorieEntity> validateAndUpdateEntity(CalorieEntity entity, CalorieView dto) {
    return Mono.just(entity)
        .flatMap(data -> validateAndUpdateSize(data, dto))
        .flatMap(data -> validateAndUpdateUnit(data, dto));
  }

  private static Mono<CalorieEntity> validateAndUpdateUnit(CalorieEntity entity, CalorieView dto) {
    return Mono.just(entity)
        .filter(
            u -> Validator.validateString(dto.unit(), 1 , 255) && AllowedCalorieUnits.CALORIE.getSymbol().equals(dto.unit()))
        .flatMap(u -> {
          u.setUnit(dto.unit());
          return Mono.just(u);
        })
        .switchIfEmpty(Mono.error(
            new BadRequestException("Invalid calorie unit. Valid ones : " + AllowedCalorieUnits.CALORIE.getSymbol()))
        );
  }

  private static Mono<CalorieEntity> validateAndUpdateSize(CalorieEntity entity, CalorieView dto) {
    return Mono.just(entity)
          .filter(u -> Validator.validateBigDecimal(dto.amount() , BigDecimal.ZERO))
        .flatMap(u -> {
          u.setAmount(dto.amount());
          return Mono.just(u);
        })
        .switchIfEmpty(Mono.error(
            new BadRequestException(ExceptionMessages.INVALID_NUMBER_MESSAGE + " Calorie amount , must be positive number"))
        );
  }
}
