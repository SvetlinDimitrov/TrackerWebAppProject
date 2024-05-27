package org.nutriGuideBuddy.service;

import org.nutriGuideBuddy.domain.dto.BadRequestException;
import org.nutriGuideBuddy.domain.dto.meal.InsertFoodDto;
import org.nutriGuideBuddy.repository.FoodRepository;
import org.nutriGuideBuddy.utils.user.UserHelperFinder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FoodServiceImp extends AbstractFoodService {

  public FoodServiceImp(FoodRepository repository, UserHelperFinder userHelper) {
    super(repository, userHelper);
  }

  public Mono<Void> deleteFoodById(String mealId, String foodId) {
    return userHelper.getUserId()
        .flatMap(userId -> getFoodEntityByIdMealIdUserId(foodId, mealId, userId))
        .flatMap(food -> repository.deleteFoodById(food.getId(), food.getMealId()));
  }

  public Mono<Void> addFoodToMeal(InsertFoodDto dto, String mealId) {
    return userHelper.getUserId()
        .flatMap(userId -> repository.findMealByIdAndUserId(mealId, userId))
        .switchIfEmpty(Mono.error(new BadRequestException("No meal found with id: " + mealId)))
        .flatMap(mealEntity -> createAndGetFood(mealEntity.getUserId(), dto, mealEntity.getId()))
        .flatMap(data -> saveFoodEntity(data.getT1(), data.getT2(), data.getT3(), data.getT4(), data.getT5()));
  }

  public Mono<Void> changeFood(String mealId, String foodId, InsertFoodDto dto) {
    return userHelper.getUserId()
        .flatMap(userId -> getFoodEntityByIdMealIdUserId(foodId, mealId, userId))
        .flatMap(food -> createAndGetFood(food.getUserId(), dto, mealId))
        .flatMap(data -> repository.deleteFoodById(foodId, mealId)
            .then(saveFoodEntity(data.getT1(), data.getT2(), data.getT3(), data.getT4(), data.getT5())));
  }
}
