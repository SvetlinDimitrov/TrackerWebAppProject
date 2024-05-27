package org.nutriGuideBuddy.service;

import org.nutriGuideBuddy.domain.dto.meal.FoodView;
import org.nutriGuideBuddy.domain.dto.meal.InsertFoodDto;
import org.nutriGuideBuddy.domain.dto.meal.ShortenFood;
import org.nutriGuideBuddy.repository.FoodRepository;
import org.nutriGuideBuddy.utils.user.UserHelperFinder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomFoodServiceImp extends AbstractFoodService {

  public CustomFoodServiceImp(FoodRepository repository, UserHelperFinder userHelper) {
    super(repository, userHelper);
  }

  public Flux<FoodView> getAllFoods() {
    return userHelper.getUserId()
        .flatMapMany(userId -> repository.findAllByFoodsByUserIdAndMealId(userId, null)
            .flatMap(foodEntity -> toFoodView(foodEntity, null)));
  }

  public Mono<FoodView> getById(String foodId) {
    return userHelper.getUserId()
        .flatMap(userId -> getFoodEntityByIdMealIdUserId(foodId, null, userId))
        .flatMap(foodEntity -> toFoodView(foodEntity, null));
  }

  public Mono<Void> deleteFood(String foodId) {
    return userHelper.getUserId()
        .flatMap(userId -> getFoodEntityByIdMealIdUserId(foodId, null, userId))
        .flatMap(entity -> repository.deleteFoodById(entity.getId(), null));
  }

  public Mono<Void> createFood(InsertFoodDto dto) {
    return userHelper.getUserId()
        .flatMap(userId -> createAndGetFood(userId, dto, null))
        .flatMap(data -> saveFoodEntity(data.getT1(), data.getT2(), data.getT3(), data.getT4(), data.getT5()));
  }

  public Mono<FoodView> changeFood(String foodId, InsertFoodDto dto) {
    return
        userHelper.getUserId()
            .flatMap(userId -> getFoodEntityByIdMealIdUserId(foodId, null, userId))
            .flatMap(food -> createAndGetFood(food.getUserId(), dto, null))
            .flatMap(data ->
                repository.deleteFoodById(foodId, null)
                    .then(saveFoodEntity(data.getT1(), data.getT2(), data.getT3(), data.getT4(), data.getT5()))
                    .then(getById(data.getT1().getId()))
            );
  }

  public Flux<ShortenFood> getAllFoodsShort() {
    return userHelper.getUserId()
        .flatMapMany(userId -> repository.findAllByFoodsByUserIdAndMealId(userId, null))
        .map(foodView -> {
          ShortenFood shortenFood = new ShortenFood();
          shortenFood.setId(foodView.getId());
          shortenFood.setName(foodView.getName());
          return shortenFood;
        });
  }
}
