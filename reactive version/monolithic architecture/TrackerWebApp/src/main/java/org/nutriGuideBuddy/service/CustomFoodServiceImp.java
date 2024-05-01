package org.nutriGuideBuddy.service;

import org.nutriGuideBuddy.domain.dto.meal.FoodView;
import org.nutriGuideBuddy.domain.dto.meal.InsertFoodDto;
import org.nutriGuideBuddy.repository.FoodRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomFoodServiceImp extends AbstractFoodService {

  public CustomFoodServiceImp(FoodRepository repository) {
    super(repository);
  }

  public Flux<FoodView> getAllFoods(String userId) {
    return repository.findAllByFoodsByUserIdAndMealId(userId, null)
        .flatMap(foodEntity -> toFoodView(foodEntity, null));
  }

  public Mono<FoodView> getById(String userId, String foodId) {
    return getFoodEntityByIdMealIdUserId(foodId, null, userId)
        .flatMap(foodEntity -> toFoodView(foodEntity, null));
  }

  public Mono<Void> deleteFood(String userId, String foodId) {
    return getFoodEntityByIdMealIdUserId(foodId, null, userId)
        .flatMap(entity -> repository.deleteFoodById(entity.getId() , null));
  }

  public Mono<Void> createFood(String userId, InsertFoodDto dto) {
    return createAndGetFood(userId, dto, null)
        .flatMap(data -> saveFoodEntity(data.getT1(), data.getT2(), data.getT3(), data.getT4(), data.getT5()));
  }

  public Mono<FoodView> changeFood(String userId, String foodId, InsertFoodDto dto) {
    return
        getFoodEntityByIdMealIdUserId(foodId, null, userId)
            .flatMap(food -> createAndGetFood(userId, dto, null))
            .flatMap(data ->
                repository.deleteFoodById(foodId , null)
                    .then(saveFoodEntity(data.getT1(), data.getT2(), data.getT3(), data.getT4(), data.getT5()))
                    .then(getById(userId, data.getT1().getId()))
        );
  }

}
