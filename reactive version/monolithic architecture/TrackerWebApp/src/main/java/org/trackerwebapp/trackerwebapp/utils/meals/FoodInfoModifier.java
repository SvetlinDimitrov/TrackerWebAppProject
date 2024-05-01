package org.trackerwebapp.trackerwebapp.utils.meals;

import org.trackerwebapp.trackerwebapp.domain.dto.BadRequestException;
import org.trackerwebapp.trackerwebapp.domain.dto.meal.FoodInfoView;
import org.trackerwebapp.trackerwebapp.domain.entity.FoodInfoEntity;
import org.trackerwebapp.trackerwebapp.domain.enums.ExceptionMessages;
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
    if(dto == null || dto.info() == null){
      return Mono.just(entity);
    }
    return Mono.just(entity)
        .filter(u -> Validator.validateString(dto.info(), 1 , 255))
        .flatMap(u -> {
          u.setInfo(dto.info());
          return Mono.just(u);
        })
        .switchIfEmpty(Mono.error(
            new BadRequestException(ExceptionMessages.INVALID_STRING_LENGTH_MESSAGE.getMessage() + "for info size."))
        );
  }

  private static Mono<FoodInfoEntity> validateAndUpdateAdditionInfo(FoodInfoEntity entity, FoodInfoView dto) {
    if(dto == null || dto.largeInfo() == null){
      return Mono.just(entity);
    }
    return Mono.just(entity)
        .filter(u -> Validator.validateString(dto.largeInfo(), 1 , 65535))
        .flatMap(u -> {
          u.setLargeInfo(dto.largeInfo());
          return Mono.just(u);
        })
        .switchIfEmpty(Mono.error(
            new BadRequestException(ExceptionMessages.INVALID_TEXT_LENGTH_MESSAGE.getMessage() + "for largeInfo."))
        );
  }

  private static Mono<FoodInfoEntity> validateAndUpdatePicture(FoodInfoEntity entity, FoodInfoView dto) {
    if(dto == null || dto.picture() == null){
      return Mono.just(entity);
    }

    String imageUrlRegex = "\\bhttps?://\\S+\\.(?:png|jpe?g|gif|bmp|svg)(?:\\?\\S*)?\\b";
    String imageUrlRegex2 = "\\bhttps?://(?:\\w+\\.)?assets\\.syndigo\\.com/(?:\\w+-){4}\\w+\\b";


    return Mono.just(entity)
        .filter(u -> !dto.picture().isBlank() && (dto.picture().matches(imageUrlRegex) || dto.picture().matches(imageUrlRegex2)))
        .flatMap(u -> {
          u.setPicture(dto.picture());
          return Mono.just(u);
        })
        .switchIfEmpty(Mono.error(
            new BadRequestException("Invalid picture"))
        );
  }
}
