package org.food.infrastructure.mappers;

import java.util.List;
import org.example.domain.food.enums.AllowedCalorieUnits;
import org.example.domain.food.nutriox_api.FoodItem;
import org.example.domain.food.shared.CalorieView;
import org.example.domain.food.shared.FoodCreateRequest;
import org.example.domain.food.shared.FoodInfoView;
import org.example.domain.food.shared.FoodView;
import org.example.domain.food.shared.NutritionView;
import org.example.domain.food.shared.ServingView;
import org.food.features.custom.entity.CustomFood;
import org.example.domain.food.mappers.FoodInfoMapper;
import org.example.domain.food.mappers.NutrientMapper;
import org.example.domain.food.mappers.ServingMapper;
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
@DecoratedWith(FoodMapperDecoder.class)
public interface FoodMapper {

  @Mapping(target = "calories.calorieAmount", source = "calories.amount")
  @Mapping(target = "calories.calorieUnit", source = "calories.unit")
  @Mapping(target = "servingPortions", ignore = true)
  CustomFood toEntity(FoodCreateRequest dto);

  @Mapping(target = "calories.amount", source = "calories.calorieAmount")
  @Mapping(target = "calories.unit", source = "calories.calorieUnit")
  @Mapping(target = "otherServing", ignore = true)
  @Mapping(target = "mainServing", ignore = true)
  FoodView toView(CustomFood entity);

  default FoodView toView(FoodItem item) {
    CalorieView calorieView = new CalorieView(
        item.getNfCalories(), AllowedCalorieUnits.CALORIE.getSymbol()
    );
    FoodInfoView foodInfoView = FoodInfoMapper.generateFoodInfo(item);
    List<NutritionView> nutrients = NutrientMapper.getNutrients(item);
    List<ServingView> servings = ServingMapper.getServings(item);
    ServingView mainServing = ServingMapper.getMainServing(item);

    return new FoodView(
        item.getFoodName(),
        calorieView,
        mainServing,
        foodInfoView,
        servings,
        nutrients
    );
  }
}
