package org.food.infrastructure.mappers;

import java.util.ArrayList;
import java.util.List;
import org.example.domain.food.shared.FoodRequest;
import org.example.domain.food.shared.OwnedFoodView;
import org.example.domain.food.shared.ServingRequest;
import org.example.domain.food.shared.ServingView;
import org.food.features.custom.entity.CustomFood;
import org.food.features.custom.entity.ServingPortion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public abstract class FoodMapperDecoder implements FoodMapper {

  private FoodMapper delegate;

  @Override
  public CustomFood toEntity(FoodRequest dto) {
    CustomFood entity = delegate.toEntity(dto);

    entity.getNutrients()
        .forEach(nutrient -> nutrient.setFood(entity));

    ServingPortion mainServing = toEntity(dto.mainServing(), entity);
    mainServing.setMain(true);

    List<ServingPortion> servingPortions = new ArrayList<>(dto.otherServing()
        .stream()
        .map(serving -> toEntity(serving, entity))
        .toList());
    servingPortions.add(mainServing);

    entity.setServingPortions(servingPortions);

    return entity;
  }

  @Override
  public OwnedFoodView toView(CustomFood entity) {
    var view = delegate.toView(entity);

    List<ServingView> otherServing = entity.getServingPortions()
        .stream()
        .filter(serving -> !serving.isMain())
        .map(this::toDto)
        .toList();

    var mainServing = entity.getServingPortions()
        .stream()
        .filter(ServingPortion::isMain)
        .map(this::toDto)
        .findFirst()
        .orElseThrow();

    return new OwnedFoodView(
        view.id(),
        view.name(),
        view.calories(),
        mainServing,
        view.foodDetails(),
        otherServing,
        view.nutrients()
    );
  }

  @Autowired
  public void setDelegate(@Qualifier("delegate") FoodMapper delegate) {
    this.delegate = delegate;
  }

  private ServingPortion toEntity(ServingRequest dto, CustomFood food) {
    ServingPortion entity = new ServingPortion();
    entity.setAmount(dto.amount());
    entity.setServingWeight(dto.servingWeight());
    entity.setMetric(dto.metric());
    entity.setMain(false);
    entity.setFood(food);
    return entity;
  }

  private ServingView toDto(ServingPortion entity) {
    return new ServingView(entity.getAmount(), entity.getServingWeight(), entity.getMetric());
  }
}
