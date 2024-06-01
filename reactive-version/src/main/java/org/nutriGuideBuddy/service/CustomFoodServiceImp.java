package org.nutriGuideBuddy.service;

import org.nutriGuideBuddy.domain.dto.meal.FoodView;
import org.nutriGuideBuddy.domain.dto.meal.InsertFoodDto;
import org.nutriGuideBuddy.domain.dto.meal.ShortenFood;
import org.nutriGuideBuddy.repository.FoodRepository;
import org.nutriGuideBuddy.utils.user.UserHelperFinder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomFoodServiceImp extends AbstractFoodService {

  public CustomFoodServiceImp(FoodRepository repository, UserHelperFinder userHelper) {
    super(repository, userHelper);
  }

  public Mono<Page<FoodView>> getAllFoods(Pageable pageable) {
    return userHelper.getUserId()
        .flatMap(userId -> repository.findAllByFoodsByUserIdAndMealIdPageable(userId, null, pageable)
            .flatMap(page -> Flux.fromIterable(page)
                .flatMap(food -> toFoodView(food, null))
                .collectList()
                .map(foodViews -> new PageImpl<>(foodViews, pageable, page.getTotalElements()))));
  }

  public Mono<Page<ShortenFood>> getAllFoodsShort(Pageable pageable) {
    return userHelper.getUserId()
        .flatMap(userId -> repository.findAllByFoodsByUserIdAndMealIdPageable(userId, null, pageable)
            .flatMap(page -> Flux.fromIterable(page)
                .flatMap(food -> repository.findCalorieByFoodId(food.getId(), food.getMealId())
                    .map(calorie -> new ShortenFood(food.getId(), food.getName(), calorie.getAmount())))
                .collectList()
                .map(foodViews -> new PageImpl<>(foodViews, pageable, page.getTotalElements()))));
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
}
