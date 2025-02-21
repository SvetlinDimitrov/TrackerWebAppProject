package org.food.infrastructure.mappers;

import org.example.domain.food.custom.dto.CustomFoodRequestCreate;
import org.example.domain.food.custom.dto.CustomFoodView;
import org.example.domain.food.custom.entity.CustomFoodEntity;
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
