package org.food.infrastructure.mappers;

import org.food.features.embedded.dto.BrandedFoodView;
import org.food.features.embedded.entity.BrandedFoodEntity;
import org.food.features.custom.dto.CustomFoodView;
import org.food.features.custom.entity.CustomFoodEntity;
import org.food.features.embedded.dto.FinalFoodView;
import org.food.features.embedded.entity.FinalFoodEntity;
import org.food.features.shared.dto.FoodView;
import org.food.features.shared.entity.FoodBaseEntity;
import org.food.features.embedded.dto.SurveyFoodView;
import org.food.features.embedded.entity.SurveyFoodEntity;
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
