package org.trackerwebapp.trackerwebapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.trackerwebapp.trackerwebapp.domain.dto.BadRequestException;
import org.trackerwebapp.trackerwebapp.domain.dto.meal.CalorieView;
import org.trackerwebapp.trackerwebapp.domain.dto.meal.InsertFoodDto;
import org.trackerwebapp.trackerwebapp.domain.dto.meal.NutritionView;
import org.trackerwebapp.trackerwebapp.domain.entity.CalorieEntity;
import org.trackerwebapp.trackerwebapp.domain.entity.FoodEntity;
import org.trackerwebapp.trackerwebapp.domain.entity.NutritionEntity;
import org.trackerwebapp.trackerwebapp.repository.CalorieRepository;
import org.trackerwebapp.trackerwebapp.repository.FoodRepository;
import org.trackerwebapp.trackerwebapp.repository.MealRepository;
import org.trackerwebapp.trackerwebapp.repository.NutritionRepository;
import org.trackerwebapp.trackerwebapp.utils.meals.CalorieModifier;
import org.trackerwebapp.trackerwebapp.utils.meals.FoodModifier;
import org.trackerwebapp.trackerwebapp.utils.meals.NutritionModifier;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodService {

  private final MealRepository mealRepository;
  private final FoodRepository repository;
  private final NutritionRepository nutritionRepository;
  private final CalorieRepository calorieRepository;

  public Mono<Void> deleteFoodById(String userId, String mealId, String foodId) {
    return getFoodEntityByIdMealIdUserId(foodId, mealId, userId)
        .flatMap(food -> repository.deleteById(food.getId()));
  }

  public Mono<Void> addFoodToMeal(String userId, InsertFoodDto dto, String mealId) {
    return mealRepository.findByIdAndUserId(mealId, userId)
        .switchIfEmpty(
            Mono.error(new BadRequestException("No meal found with id: " + mealId)))
        .flatMap(
            mealEntity -> createAndFillFoodEntity(dto, mealEntity.getId(), mealEntity.getUserId())
        )
        .flatMap(foodEntity -> Mono.zip(
            Mono.just(foodEntity),
            createAndFillNutritions(dto.nutrients(), foodEntity.getId(), foodEntity.getUserId()),
            createAndFillCalorieEntity(dto.calories(), foodEntity.getId(), foodEntity.getMealId(),
                foodEntity.getUserId())
        ))
        .flatMap(data ->
            repository.save(data.getT1())
                .then(calorieRepository.save(data.getT3()))
                .thenMany(nutritionRepository.saveAll(data.getT2()))
                .then()
        );
  }

  public Mono<Void> changeFood(String userId, String mealId, String foodId, InsertFoodDto dto) {
    return getFoodEntityByIdMealIdUserId(foodId, mealId, userId)
        .flatMap(foodEntityToDelete -> createAndFillFoodEntity(dto, mealId, userId))
        .flatMap(newFood -> Mono.zip(
            Mono.just(newFood),
            createAndFillNutritions(
                dto.nutrients(),
                newFood.getId(),
                newFood.getUserId()
            ),
            createAndFillCalorieEntity(
                dto.calories(),
                newFood.getId(),
                newFood.getMealId(),
                newFood.getUserId()
            )
        ))
        .flatMap(data ->
            repository.deleteById(foodId)
                .then(repository.save(data.getT1()))
                .then(calorieRepository.save(data.getT3()))
                .thenMany(nutritionRepository.saveAll(data.getT2()))
                .then()
        );
  }

  private Mono<FoodEntity> getFoodEntityByIdMealIdUserId(String foodId, String mealId, String userId) {
    return repository.findByIdAndMealIdAndUserId(foodId, mealId, userId)
        .switchIfEmpty(
            Mono.error(
                new BadRequestException(
                    "No food found with id: "
                        + foodId + " in meal with id: "
                        + mealId
                )
            )
        );
  }

  private Mono<CalorieEntity> createAndFillCalorieEntity(CalorieView dto, String foodId, String mealId, String userId) {
    if (dto == null) {
      return Mono.error(new BadRequestException("calorie cannot be null"));
    }
    return Mono.just(new CalorieEntity())
        .flatMap(entity -> {
          entity.setFoodId(foodId);
          entity.setMealId(mealId);
          entity.setUserId(userId);
          return Mono.just(entity);
        })
        .flatMap(calorieEntity -> CalorieModifier.validateAndUpdateUnit(calorieEntity, dto))
        .flatMap(calorieEntity -> CalorieModifier.validateAndUpdateSize(calorieEntity, dto));
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

  private Mono<FoodEntity> createAndFillFoodEntity(InsertFoodDto dto, String mealId, String userId) {
    if (dto == null) {
      return Mono.error(new BadRequestException("food cannot be null"));
    }
    return Mono.just(new FoodEntity())
        .flatMap(foodEntity -> FoodModifier.validateAndUpdateMeasurement(foodEntity, dto))
        .flatMap(foodEntity -> FoodModifier.validateAndUpdateName(foodEntity, dto))
        .flatMap(foodEntity -> FoodModifier.validateAndUpdateSize(foodEntity, dto))
        .flatMap(foodEntity -> {
          foodEntity.setMealId(mealId);
          foodEntity.setUserId(userId);
          return Mono.just(foodEntity);
        });
  }

}
