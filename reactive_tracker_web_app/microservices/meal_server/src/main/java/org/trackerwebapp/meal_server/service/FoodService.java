package org.trackerwebapp.meal_server.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.trackerwebapp.meal_server.domain.dto.CalorieView;
import org.trackerwebapp.meal_server.domain.dto.InsertFoodDto;
import org.trackerwebapp.meal_server.domain.dto.NutritionView;
import org.trackerwebapp.meal_server.domain.entity.CalorieEntity;
import org.trackerwebapp.meal_server.domain.entity.FoodEntity;
import org.trackerwebapp.meal_server.domain.entity.NutritionEntity;
import org.trackerwebapp.meal_server.repository.CalorieRepository;
import org.trackerwebapp.meal_server.repository.FoodRepository;
import org.trackerwebapp.meal_server.repository.MealRepository;
import org.trackerwebapp.meal_server.repository.NutritionRepository;
import org.trackerwebapp.meal_server.utils.CalorieModifier;
import org.trackerwebapp.meal_server.utils.FoodModifier;
import org.trackerwebapp.meal_server.utils.NutritionModifier;
import org.trackerwebapp.shared_interfaces.domain.exception.BadRequestException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

  public Mono<Void> changeFood(String userId, String mealId, String foodId,
      InsertFoodDto dto) {
    return getFoodEntityByIdMealIdUserId(foodId, mealId, userId)
        .flatMap(foodEntityToDelete -> Mono.zip(
            Mono.just(foodEntityToDelete),
            createAndFillFoodEntity(
                dto,
                foodEntityToDelete.getMealId(),
                foodEntityToDelete.getUserId()
            ),
            createAndFillNutritions(
                dto.nutrients(),
                foodEntityToDelete.getId(),
                foodEntityToDelete.getUserId()
            ),
            createAndFillCalorieEntity(
                dto.calories(),
                foodEntityToDelete.getId(),
                foodEntityToDelete.getMealId(),
                foodEntityToDelete.getUserId()
            )
        ))
        .flatMap(data ->
            repository.deleteById(foodId)
                .then(repository.save(data.getT2()))
                .then(calorieRepository.save(data.getT4()))
                .thenMany(nutritionRepository.saveAll(data.getT3()))
                .then()
        );
  }

  private Mono<FoodEntity> getFoodEntityByIdMealIdUserId(
      String foodId,
      String mealId,
      String userId) {
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

  private Mono<CalorieEntity> createAndFillCalorieEntity(CalorieView dto, String foodId,
      String mealId,
      String userId) {
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

  private Mono<List<NutritionEntity>> createAndFillNutritions(List<NutritionView> dtoList,
      String foodId,
      String userId) {
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

  private Mono<FoodEntity> createAndFillFoodEntity(
      InsertFoodDto dto,
      String mealId,
      String userId) {
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
