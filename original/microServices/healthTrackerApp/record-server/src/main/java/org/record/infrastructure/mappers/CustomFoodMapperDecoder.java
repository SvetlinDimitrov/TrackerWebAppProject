package org.record.infrastructure.mappers;

import org.example.domain.food.custom.dto.CustomFoodRequestCreate;
import org.example.domain.food.custom.entity.CustomFoodEntity;
import org.example.domain.food.shared.entity.Calories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public abstract class CustomFoodMapperDecoder implements CustomFoodMapper {

  private CustomFoodMapper delegate;

  @Override
  public CustomFoodEntity toEntity(CustomFoodRequestCreate dto) {
    var entity = delegate.toEntity(dto);
    entity.setCalories(new Calories(dto.calories()));
    return entity;
  }

  @Autowired
  public void setDelegate(@Qualifier("delegate") CustomFoodMapper delegate) {
    this.delegate = delegate;
  }
}
