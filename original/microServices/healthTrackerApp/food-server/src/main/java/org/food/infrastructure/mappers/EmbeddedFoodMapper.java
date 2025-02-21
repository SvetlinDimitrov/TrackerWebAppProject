package org.food.infrastructure.mappers;

import org.example.domain.food.embedded.dto.BrandedFoodView;
import org.example.domain.food.embedded.entity.BrandedFoodEntity;
import org.example.domain.food.custom.dto.CustomFoodView;
import org.example.domain.food.custom.entity.CustomFoodEntity;
import org.example.domain.food.embedded.dto.FinalFoodView;
import org.example.domain.food.embedded.entity.FinalFoodEntity;
import org.example.domain.food.shared.dto.FoodView;
import org.example.domain.food.shared.entity.FoodBaseEntity;
import org.example.domain.food.embedded.dto.SurveyFoodView;
import org.example.domain.food.embedded.entity.SurveyFoodEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface EmbeddedFoodMapper {

  @SuppressWarnings("unchecked")
  default <T extends FoodView, E extends FoodBaseEntity> T toView(E entity, Class<T> viewType) {
    if (viewType.equals(BrandedFoodView.class)) {
      return (T) toBrandedView((BrandedFoodEntity) entity);
    } else if (viewType.equals(FinalFoodView.class)) {
      return (T) toFinalView((FinalFoodEntity) entity);
    } else if (viewType.equals(SurveyFoodView.class)) {
      return (T) toSurveyView((SurveyFoodEntity) entity);
    } else if (viewType.equals(CustomFoodView.class)) {
      return (T) toCustomView((CustomFoodEntity) entity);
    } else {
      throw new IllegalArgumentException("Unsupported view type: " + viewType);
    }
  }

  BrandedFoodView toBrandedView(BrandedFoodEntity entity);

  SurveyFoodView toSurveyView(SurveyFoodEntity entity);

  FinalFoodView toFinalView(FinalFoodEntity entity);

  CustomFoodView toCustomView(CustomFoodEntity entity);
}
