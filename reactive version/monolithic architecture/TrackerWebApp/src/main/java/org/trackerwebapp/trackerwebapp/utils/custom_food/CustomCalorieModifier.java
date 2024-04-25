package org.trackerwebapp.trackerwebapp.utils.custom_food;

import org.trackerwebapp.trackerwebapp.domain.dto.BadRequestException;
import org.trackerwebapp.trackerwebapp.domain.dto.custom_food.CustomCalorieView;
import org.trackerwebapp.trackerwebapp.domain.entity.CustomCalorieEntity;
import org.trackerwebapp.trackerwebapp.utils.Validator;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class CustomCalorieModifier {

  public static Mono<CustomCalorieEntity> validateAndUpdateSize(CustomCalorieEntity entity, CustomCalorieView dto) {
    return Mono.just(entity)
        .filter(u -> Validator.validateBigDecimal(dto.amount(), BigDecimal.ONE))
        .flatMap(u -> {
          u.setAmount(dto.amount());
          return Mono.just(u);
        })
        .switchIfEmpty(Mono.error(
            new BadRequestException("Invalid calorie amount , must be greater then 0"))
        );
  }
}
