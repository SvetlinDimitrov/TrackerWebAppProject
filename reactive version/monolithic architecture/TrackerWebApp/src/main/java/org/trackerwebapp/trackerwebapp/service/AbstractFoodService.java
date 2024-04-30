package org.trackerwebapp.trackerwebapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.trackerwebapp.trackerwebapp.domain.dto.BadRequestException;
import org.trackerwebapp.trackerwebapp.domain.dto.meal.*;
import org.trackerwebapp.trackerwebapp.domain.entity.*;
import org.trackerwebapp.trackerwebapp.repository.*;
import org.trackerwebapp.trackerwebapp.utils.meals.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple5;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public abstract class AbstractFoodService {

  protected final FoodRepository repository;

  protected Mono<FoodView> toFoodView(FoodEntity entity , String mealId) {
    return Mono.zip(
            repository.findCalorieByFoodId(entity.getId() , mealId),
            repository.findAllNutritionsByFoodId(entity.getId()).collectList(),
            repository.findAllServingsByFoodId(entity.getId()).collectList(),
            repository.findFoodInfoByFoodId(entity.getId())
        )
        .flatMap(tuple -> Mono.zip(
            Mono.just(CalorieView.toView(tuple.getT1())),
            Mono.just(tuple.getT2().stream()
                .map(NutritionView::toView).toList()),
            Mono.just(tuple.getT3().stream()
                .filter(serving -> !serving.getMain())
                .map(ServingView::toView).toList()),
            Mono.just(Objects.requireNonNull(tuple.getT3().stream()
                .filter(ServingEntity::getMain)
                .findFirst()
                .map(ServingView::toView)
                .orElse(null))),
            Mono.just(FoodInfoView.toView(tuple.getT4()))
        ))
        .map(tuple -> new FoodView(entity.getId(), entity.getName(), tuple.getT5(), tuple.getT4(), tuple.getT3(), tuple.getT1(), tuple.getT2()));


  }

  protected Mono<FoodEntity> getFoodEntityByIdMealIdUserId(String foodId, String mealId, String userId) {
    return repository.findFoodByIdAndMealIdAndUserId(foodId, mealId, userId)
        .switchIfEmpty(Mono.error(new BadRequestException("No food found with id: " + foodId)));
  }

  protected Mono<Tuple5<FoodEntity, List<ServingEntity>, CalorieEntity, List<NutritionEntity>, FoodInfoEntity>> createAndGetFood(String userId, InsertFoodDto dto, String mealId) {
    return createAndFillFoodEntity(dto, userId, mealId)
        .flatMap(foodEntity -> Mono.zip(
            Mono.just(foodEntity),
            createAndFillServings(dto.mainServing(), dto.otherServing(), foodEntity.getId()),
            createAndFillCalorieEntity(dto.calories(), foodEntity.getId(), mealId, Optional.ofNullable(mealId).map((a) -> userId).orElse(null)),
            createAndFillNutritions(dto.nutrients(), foodEntity.getId(), Optional.ofNullable(mealId).map((a) -> userId).orElse(null)),
            createAndFillFoodInfoEntity(dto.foodDetails(), foodEntity.getId())
        ));
  }

  protected Mono<Void> saveFoodEntity(
      FoodEntity foodEntity,
      List<ServingEntity> servingEntities,
      CalorieEntity calorieEntity,
      List<NutritionEntity> nutritionEntities,
      FoodInfoEntity foodInfoEntity) {
    return
        repository.saveFood(foodEntity)
            .then(repository.saveCalorie(calorieEntity))
            .thenMany(repository.saveAllServings(servingEntities))
            .thenMany(repository.saveAllNutritions(nutritionEntities))
            .then(repository.saveFoodInfo(foodInfoEntity))
            .then();
  }

  private Mono<FoodEntity> createAndFillFoodEntity(InsertFoodDto dto, String userId, String mealId) {
    if (dto == null) {
      return Mono.error(new BadRequestException("food cannot be null"));
    }
    return Mono.just(new FoodEntity())
        .flatMap(foodEntity -> FoodModifier.validateAndUpdateName(foodEntity, dto))
        .flatMap(foodEntity -> {
          foodEntity.setUserId(userId);
          Optional.ofNullable(mealId)
              .ifPresent(foodEntity::setMealId);
          return Mono.just(foodEntity);
        });
  }

  private Mono<List<ServingEntity>> createAndFillServings(ServingView mainServing, List<ServingView> others, String foodId) {
    if (mainServing == null) {
      return Mono.error(new BadRequestException("Serving cannot be null"));
    }
    return Mono.just(createServingEntity(mainServing, foodId, true))
        .flatMap(calorieEntity -> ServingValidator.validateEntity(calorieEntity, mainServing))
        .flatMap(entity -> Mono.zip(
            Mono.just(entity),
            Mono.just(
                Optional.ofNullable(others)
                    .map(list -> list.stream()
                        .map(dto -> createServingEntity(dto, foodId, false))
                        .toList())
                    .orElse(List.of())
            )
        ))
        .map(data -> {
          ArrayList<ServingEntity> result = new ArrayList<>();
          result.add(data.getT1());
          result.addAll(data.getT2());
          return result;
        });
  }

  private ServingEntity createServingEntity(ServingView servingView, String foodId, boolean isMain) {
    ServingEntity entity = new ServingEntity();
    entity.setAmount(servingView.amount());
    entity.setMetric(servingView.metric());
    entity.setServingWeight(servingView.servingWeight());
    entity.setMain(isMain);
    entity.setFoodId(foodId);
    return entity;
  }

  private Mono<CalorieEntity> createAndFillCalorieEntity(CalorieView dto, String foodId, String mealId, String userId) {
    if (dto == null) {
      return Mono.error(new BadRequestException("calorie cannot be null"));
    }
    return Mono.just(new CalorieEntity())
        .flatMap(entity -> {
          entity.setFoodId(foodId);
          entity.setUserId(userId);
          entity.setMealId(mealId);
          return Mono.just(entity);
        })
        .flatMap(calorieEntity -> CalorieModifier.validateAndUpdateEntity(calorieEntity, dto));
  }

  private Mono<FoodInfoEntity> createAndFillFoodInfoEntity(FoodInfoView dto, String foodId) {
    FoodInfoEntity newEntity = new FoodInfoEntity();
    newEntity.setFoodId(foodId);

    if (dto == null) {
      return Mono.just(newEntity);
    }
    return Mono.just(newEntity)
        .flatMap(entity -> {
          entity.setFoodId(foodId);
          return Mono.just(entity);
        })
        .flatMap(calorieEntity -> FoodInfoModifier.validateAndUpdateEntity(calorieEntity, dto));
  }

  private Mono<List<NutritionEntity>> createAndFillNutritions(List<NutritionView> dtoList, String foodId, String userId) {
    if (dtoList == null) {
      return Mono.error(new BadRequestException("nutrients cannot be null"));
    }
    return Flux.fromIterable(dtoList)
        .flatMap(dto -> {
          NutritionEntity nutrition = new NutritionEntity();
          nutrition.setFoodId(foodId);
          nutrition.setUserId(userId);
          return NutritionModifier.validateAndUpdateName(nutrition, dto)
              .flatMap(updatedNutrition ->
                  NutritionModifier.validateAndUpdateUnit(updatedNutrition, dto))
              .flatMap(updatedNutrition ->
                  NutritionModifier.validateAndUpdateAmount(updatedNutrition, dto))
              .thenReturn(nutrition);
        })
        .collectList();
  }

}
