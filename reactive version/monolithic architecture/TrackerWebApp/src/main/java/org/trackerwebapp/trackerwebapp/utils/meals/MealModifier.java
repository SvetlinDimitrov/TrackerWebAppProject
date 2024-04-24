package org.trackerwebapp.trackerwebapp.utils.meals;

import org.trackerwebapp.trackerwebapp.domain.dto.BadRequestException;
import org.trackerwebapp.trackerwebapp.domain.dto.meal.CreateMeal;
import org.trackerwebapp.trackerwebapp.domain.entity.MealEntity;
import reactor.core.publisher.Mono;

public class MealModifier {

  public static Mono<MealEntity> updateName(MealEntity entity, CreateMeal dto) {
    return Mono.just(entity)
        .filter(u -> dto.name() != null && !dto.name().isBlank())
        .flatMap(u -> {
          if (dto.name().trim().length() >= 2) {
            u.setName(dto.name());
            return Mono.just(u);
          } else {
            return Mono.error(new BadRequestException("Invalid name length , must be greater then 2 chars"));
          }
        })
        .switchIfEmpty(Mono.just(entity));
  }
}
