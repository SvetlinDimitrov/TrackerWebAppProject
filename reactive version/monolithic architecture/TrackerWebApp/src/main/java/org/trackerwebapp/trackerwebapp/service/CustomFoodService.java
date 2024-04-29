package org.trackerwebapp.trackerwebapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.trackerwebapp.trackerwebapp.domain.dto.BadRequestException;
import org.trackerwebapp.trackerwebapp.domain.dto.meal.*;
import org.trackerwebapp.trackerwebapp.domain.entity.CustomCalorieEntity;
import org.trackerwebapp.trackerwebapp.domain.entity.CustomFoodEntity;
import org.trackerwebapp.trackerwebapp.domain.entity.CustomNutritionEntity;
import org.trackerwebapp.trackerwebapp.domain.entity.CustomServingEntity;
import org.trackerwebapp.trackerwebapp.repository.CalorieRepository;
import org.trackerwebapp.trackerwebapp.repository.FoodRepository;
import org.trackerwebapp.trackerwebapp.repository.NutritionRepository;
import org.trackerwebapp.trackerwebapp.repository.ServingRepository;
import org.trackerwebapp.trackerwebapp.utils.custom_food.CustomFoodModifier;
import org.trackerwebapp.trackerwebapp.utils.custom_food.CustomNutritionModifier;
import org.trackerwebapp.trackerwebapp.utils.meals.CalorieModifier;
import org.trackerwebapp.trackerwebapp.utils.meals.ServingValidator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomFoodService {

  private final FoodRepository repository;
  private final NutritionRepository nutritionRepository;
  private final CalorieRepository calorieRepository;
  private final ServingRepository servingRepository;

  public Flux<FoodView> getAllFoods(String userId) {
    return repository.findAllByUserIdCustom(userId)
        .flatMap(this::toFoodView);
  }

  public Mono<FoodView> getById(String userId, String foodId) {
    return getFoodEntityMono(userId, foodId)
        .flatMap(this::toFoodView);
  }

  public Mono<Void> deleteFood(String userId, String foodId) {
    return getFoodEntityMono(userId, foodId)
        .flatMap(entity -> repository.deleteByIdCustom(entity.getId()));
  }

  public Mono<Void> createFood(String userId, InsertFoodDto dto) {
    return createAndFillFoodEntity(dto, userId)
        .flatMap(food -> repository.findByNameCustom(food.getName())
            .flatMap(customFoodEntity -> Mono.error(new BadRequestException("Food name already exists")))
            .switchIfEmpty(Mono.just(food))
            .cast(CustomFoodEntity.class)
        )
        .flatMap(foodEntity -> Mono.zip(
            Mono.just(foodEntity),
            createAndFillCalorieEntity(dto.calories(), foodEntity.getId()),
            createAndFillNutritions(dto.nutrients(), foodEntity.getId()),
            createAndFillServings(dto.serving() , foodEntity.getId())
        ))
        .flatMap(data ->
            repository.saveCustom(data.getT1())
                .then(calorieRepository.saveCustom(data.getT2()))
                .then(servingRepository.saveCustom(data.getT4()))
                .thenMany(nutritionRepository.saveAllCustom(data.getT3()))
                .then()
        );
  }

  public Mono<FoodView> changeFood(String userId, String foodId, InsertFoodDto dto) {
    return
        repository.findByIdCustom(foodId)
            .switchIfEmpty(Mono.error(new BadRequestException("Custom Food with id: " + foodId + " does not exist")))
            .flatMap(food -> createAndFillFoodEntity(dto, userId))
            .flatMap(food -> repository.findByNameCustom(food.getName())
            .flatMap(customFoodEntity -> {
                  if (foodId.equals(customFoodEntity.getId())) {
                    return Mono.empty();
                  }
                  return Mono.error(new BadRequestException("Food name already exists"));
                }
            )
            .switchIfEmpty(Mono.just(food))
            .cast(CustomFoodEntity.class)
        )
        .flatMap(foodEntity -> Mono.zip(
            Mono.just(foodEntity),
            createAndFillCalorieEntity(dto.calories(), foodEntity.getId()),
            createAndFillNutritions(dto.nutrients(), foodEntity.getId()),
            createAndFillServings(dto.serving(), foodEntity.getId())
        ))
        .flatMap(data ->
            repository.deleteByIdCustom(foodId)
                .then(repository.saveCustom(data.getT1()))
                .then(calorieRepository.saveCustom(data.getT2()))
                .then(servingRepository.saveCustom(data.getT4()))
                .thenMany(nutritionRepository.saveAllCustom(data.getT3()))
                .then(getById(userId, data.getT1().getId()))
        );
  }

  private Mono<CustomFoodEntity> getFoodEntityMono(String userId, String foodId) {
    return repository.findByIdAndUserIdCustom(foodId, userId)
        .switchIfEmpty(
            Mono.error(new BadRequestException("No such food exists with id: " + foodId)));
  }

  private Mono<FoodView> toFoodView(CustomFoodEntity entity) {
    return calorieRepository.findByFoodIdCustom(entity.getId())
        .map(CalorieView::toView)
        .zipWith(nutritionRepository
            .findAllByFoodIdCustom(entity.getId())
            .map(NutritionView::toView)
            .collectList())
        .zipWith(servingRepository
            .findByFoodIdCustom(entity.getId())
            .map(ServingView::toView)
        )
        .map(data -> FoodView.toView(entity, data.getT1().getT2(), data.getT1().getT1(), List.of(data.getT2())));
  }

  private Mono<CustomServingEntity> createAndFillServings(List<ServingView> dto, String foodId) {
    if (dto == null || dto.isEmpty()) {
      return Mono.error(new BadRequestException("Serving cannot be null"));
    }
    return Mono.just(new CustomServingEntity())
        .flatMap(entity -> {
          entity.setFoodId(foodId);
          return Mono.just(entity);
        })
        .flatMap(calorieEntity -> ServingValidator.validateAndUpdateWeight(calorieEntity, dto.getFirst()))
        .flatMap(calorieEntity -> ServingValidator.validateAndUpdateMetric(calorieEntity, dto.getFirst()))
        .flatMap(calorieEntity -> ServingValidator.validateAndUpdateAmount(calorieEntity, dto.getFirst()));
  }

  private Mono<CustomCalorieEntity> createAndFillCalorieEntity(CalorieView dto, String foodId) {
    if (dto == null) {
      return Mono.error(new BadRequestException("calorie cannot be null"));
    }
    return Mono.just(new CustomCalorieEntity())
        .flatMap(entity -> {
          entity.setFoodId(foodId);
          return Mono.just(entity);
        })
        .flatMap(calorieEntity -> CalorieModifier.validateAndUpdateSize(calorieEntity, dto));
  }

  private Mono<List<CustomNutritionEntity>> createAndFillNutritions(List<NutritionView> dtoList, String foodId) {
    if (dtoList == null) {
      return Mono.error(new BadRequestException("nutrients cannot be null"));
    }
    return Flux.fromIterable(dtoList)
        .flatMap(dto -> {
          CustomNutritionEntity nutrition = new CustomNutritionEntity();
          nutrition.setFoodId(foodId);
          return CustomNutritionModifier.validateAndUpdateName(nutrition, dto)
              .flatMap(updatedNutrition ->
                  CustomNutritionModifier.validateAndUpdateUnit(updatedNutrition, dto))
              .flatMap(updatedNutrition ->
                  CustomNutritionModifier.validateAndUpdateAmount(updatedNutrition, dto))
              .thenReturn(nutrition);
        })
        .collectList();
  }

  private Mono<CustomFoodEntity> createAndFillFoodEntity(InsertFoodDto dto, String userId) {
    if (dto == null) {
      return Mono.error(new BadRequestException("food cannot be null"));
    }
    return Mono.just(new CustomFoodEntity())
        .flatMap(foodEntity -> CustomFoodModifier.validateAndUpdateName(foodEntity, dto))
        .flatMap(foodEntity -> {
          foodEntity.setUserId(userId);
          return Mono.just(foodEntity);
        });
  }
}
