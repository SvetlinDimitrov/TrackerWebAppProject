package org.trackerwebapp.trackerwebapp.utils.meals;

import org.trackerwebapp.trackerwebapp.domain.dto.BadRequestException;
import org.trackerwebapp.trackerwebapp.domain.dto.meal.FoodInfoView;
import org.trackerwebapp.trackerwebapp.domain.entity.FoodInfoEntity;
import org.trackerwebapp.trackerwebapp.utils.Validator;
import reactor.core.publisher.Mono;

public class FoodInfoModifier {

  public static Mono<FoodInfoEntity> validateAndUpdateEntity(FoodInfoEntity entity, FoodInfoView dto) {
    return Mono.just(entity)
        .flatMap(data -> validateAndUpdateInfo(data, dto))
        .flatMap(data -> validateAndUpdateAdditionInfo(data, dto))
        .flatMap(data -> validateAndUpdatePicture(data, dto));
  }

  private static Mono<FoodInfoEntity> validateAndUpdateInfo(FoodInfoEntity entity, FoodInfoView dto) {
    return Mono.just(entity)
        .filter(u -> Validator.validateString(dto.info(), 2))
        .flatMap(u -> {
          u.setInfo(dto.info());
          return Mono.just(u);
        })
        .switchIfEmpty(Mono.error(
            new BadRequestException("Invalid info size must be at least 2 chars"))
        );
  }

  private static Mono<FoodInfoEntity> validateAndUpdateAdditionInfo(FoodInfoEntity entity, FoodInfoView dto) {
    return Mono.just(entity)
        .filter(u -> Validator.validateString(dto.moreInfo(), 2))
        .flatMap(u -> {
          u.setMoreInfo(dto.moreInfo());
          return Mono.just(u);
        })
        .switchIfEmpty(Mono.error(
            new BadRequestException("Invalid moreInfo size must be at least 2 chars"))
        );
  }

  private static Mono<FoodInfoEntity> validateAndUpdatePicture(FoodInfoEntity entity, FoodInfoView dto) {
    return Mono.just(entity)
        .filter(u -> Validator.validateString(dto.picture(), 2))
        .flatMap(u -> {
          u.setPicture(dto.picture());
          return Mono.just(u);
        })
        .switchIfEmpty(Mono.error(
            new BadRequestException("Invalid picture"))
        );
  }
}
