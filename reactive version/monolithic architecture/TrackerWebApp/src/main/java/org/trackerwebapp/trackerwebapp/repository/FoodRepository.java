package org.trackerwebapp.trackerwebapp.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.trackerwebapp.trackerwebapp.domain.entity.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class FoodRepository {

  private final R2dbcEntityTemplate entityTemplate;

  public Mono<FoodEntity> findFoodByIdAndMealIdAndUserId(String foodId, String mealId, String userId) {

    return Optional.ofNullable(mealId)
        .map(id -> entityTemplate.selectOne(
            query(where("id").is(foodId)
                .and("mealId").is(id)
                .and("userId").is(userId)
            ), FoodEntity.class
        ))
        .orElse( entityTemplate.selectOne(
            query(where("id").is(foodId)
                .and("mealId").isNull()
                .and("userId").is(userId)
            ), FoodEntity.class
        ));
  }

  public Flux<FoodEntity> findAllFoodsByMealId(String mealId) {
    return entityTemplate.select(
        query(where("mealId").is(mealId)), FoodEntity.class
    );
  }

  public Flux<FoodEntity> findAllByFoodsByUserIdAndMealId(String userId, String mealId) {
    return Optional.ofNullable(mealId)
        .map(id -> entityTemplate.select(
            query(where("userId").is(userId)
                .and("mealId").is(mealId)), FoodEntity.class
        ))
        .orElse(entityTemplate.select(
            query(where("userId").is(userId)
                .and("mealId").isNull()), FoodEntity.class
        ));
  }

  @Modifying
  public Mono<Void> deleteFoodById(String foodId, String mealId) {

    return Optional.ofNullable(mealId)
        .map(id ->
            entityTemplate.delete(FoodEntity.class)
                .matching(query(where("id").is(foodId)
                    .and("mealId").is(id)))
                .all()
                .then())
        .orElse(
            entityTemplate.delete(FoodEntity.class)
                .matching(query(where("id").is(foodId)))
                .all()
                .then());
  }

  public Mono<FoodEntity> saveFood(FoodEntity entity) {
    return entityTemplate.insert(entity);
  }

  public Mono<CalorieEntity> saveCalorie(CalorieEntity entity) {
    return entityTemplate.insert(entity);
  }

  public Mono<CalorieEntity> findCalorieByFoodId(String foodId, String mealId) {
    return Optional.ofNullable(mealId)
        .map(id -> entityTemplate.selectOne(
            query(where("foodId").is(foodId)
                .and("mealId").is(id)), CalorieEntity.class
        ))
        .orElse(entityTemplate.selectOne(
            query(where("foodId").is(foodId)
                .and("mealId").isNull()), CalorieEntity.class
        ));
  }

  public Mono<FoodInfoEntity> saveFoodInfo(FoodInfoEntity entity) {
    return entityTemplate.insert(entity);
  }

  public Mono<FoodInfoEntity> findFoodInfoByFoodId(String foodId) {
    return entityTemplate.selectOne(
        query(where("foodId").is(foodId)), FoodInfoEntity.class
    );
  }

  public Flux<NutritionEntity> saveAllNutritions(List<NutritionEntity> entities) {
    return Flux.fromIterable(entities)
        .flatMapSequential(entityTemplate::insert);
  }

  public Flux<NutritionEntity> findAllNutritionsByFoodId(String foodId) {
    return entityTemplate.select(
        query(where("foodId").is(foodId)), NutritionEntity.class
    );
  }

  public Flux<ServingEntity> findAllServingsByFoodId(String foodId) {
    return entityTemplate.select(
        query(where("foodId").is(foodId)), ServingEntity.class
    );
  }

  public Flux<ServingEntity> saveAllServings(List<ServingEntity> entities) {
    return Flux.fromIterable(entities)
        .flatMapSequential(entityTemplate::insert);
  }

  public Mono<MealEntity> findMealByIdAndUserId(String id, String userId) {
    return entityTemplate.selectOne(
        query(where("userId").is(userId)
            .and("id").is(id)
        ), MealEntity.class
    );
  }
}
