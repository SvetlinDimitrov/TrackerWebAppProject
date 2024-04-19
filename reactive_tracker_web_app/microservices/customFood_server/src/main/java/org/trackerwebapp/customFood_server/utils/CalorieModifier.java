package org.trackerwebapp.customFood_server.utils;

import java.math.BigDecimal;
import org.trackerwebapp.customFood_server.domain.dto.CalorieView;
import org.trackerwebapp.customFood_server.domain.entity.CalorieEntity;
import org.trackerwebapp.shared_interfaces.domain.enums.AllowedCalorieUnits;
import org.trackerwebapp.shared_interfaces.domain.exception.BadRequestException;
import org.trackerwebapp.shared_interfaces.domain.utils.Validator;
import reactor.core.publisher.Mono;

public class CalorieModifier {

  public static Mono<CalorieEntity> validateAndUpdateSize(CalorieEntity entity, CalorieView dto) {
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
