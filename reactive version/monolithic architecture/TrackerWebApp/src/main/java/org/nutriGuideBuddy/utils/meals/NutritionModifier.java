package org.nutriGuideBuddy.utils.meals;

import org.nutriGuideBuddy.domain.dto.BadRequestException;
import org.nutriGuideBuddy.domain.dto.meal.NutritionView;
import org.nutriGuideBuddy.domain.entity.NutritionEntity;
import org.nutriGuideBuddy.domain.enums.AllowedNutrients;
import org.nutriGuideBuddy.domain.enums.ExceptionMessages;
import org.nutriGuideBuddy.utils.Validator;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Arrays;

public class NutritionModifier {

  public static Mono<NutritionEntity> validateAndUpdateEntity(NutritionView dto, String foodId, String userId) {

    NutritionEntity entity = new NutritionEntity();
    entity.setUserId(userId);
    entity.setFoodId(foodId);

    return Mono.just(entity)
        .flatMap(data -> validateAndUpdateName(data, dto))
        .flatMap(data -> validateAndUpdateUnit(data, dto))
        .flatMap(data -> validateAndUpdateAmount(data, dto));
  }

  private static Mono<NutritionEntity> validateAndUpdateName(NutritionEntity entity, NutritionView dto) {
    return Mono.just(entity)
        .filter(u -> dto.name() != null &&
            Arrays.stream(AllowedNutrients.values())
                .anyMatch(correctValue -> correctValue.getNutrientName().equals(dto.name())))
        .flatMap(u -> {
          u.setName(dto.name());
          return Mono.just(u);
        })
        .switchIfEmpty(Mono.error(
            new BadRequestException("Invalid nutrition name: " + dto.name()))
        );
  }

  private static Mono<NutritionEntity> validateAndUpdateUnit(NutritionEntity entity, NutritionView dto) {
    return Mono.just(entity)
        .filter(u -> dto.name() != null && dto.unit() != null &&
            Arrays.stream(AllowedNutrients.values())
                .anyMatch(correctValue -> correctValue.getNutrientName().equals(dto.name())
                    && correctValue.getNutrientUnit().equals(dto.unit()))
        )
        .flatMap(u -> {
          u.setUnit(dto.unit());
          return Mono.just(u);
        })
        .switchIfEmpty(Mono.error(
            new BadRequestException("Invalid nutrition unit: " + dto.unit() + " for name : " + dto.name()))
        );
  }

  private static Mono<NutritionEntity> validateAndUpdateAmount(NutritionEntity entity, NutritionView dto) {
    return Mono.just(entity)
        .filter(data -> Validator.validateBigDecimal(dto.amount(), BigDecimal.ZERO))
        .flatMap(u -> {
          u.setAmount(dto.amount());
          return Mono.just(u);
        })
        .switchIfEmpty(Mono.error(
            new BadRequestException(ExceptionMessages.INVALID_NUMBER_MESSAGE.getMessage() + "for nutrition amount , must be higher than 0"))
        );
  }
}
