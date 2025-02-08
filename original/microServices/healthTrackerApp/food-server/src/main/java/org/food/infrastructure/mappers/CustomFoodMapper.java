package org.food.infrastructure.mappers;

import org.food.features.custom.dto.CustomFoodRequestCreate;
import org.food.features.custom.dto.CustomFoodView;
import org.food.features.custom.entity.CustomFoodEntity;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
@DecoratedWith(CustomFoodMapperDecoder.class)
public interface CustomFoodMapper {

  @Mapping(target = "calories", ignore = true)
  CustomFoodEntity toEntity(CustomFoodRequestCreate dto);

  CustomFoodView toView(CustomFoodEntity entity);
}
