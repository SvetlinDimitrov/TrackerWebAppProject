package org.trackerwebapp.trackerwebapp.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import org.trackerwebapp.trackerwebapp.domain.entity.CalorieEntity;
import org.trackerwebapp.trackerwebapp.domain.entity.CustomCalorieEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class CalorieRepository {

  private final R2dbcEntityTemplate entityTemplate;

  public Mono<CalorieEntity> save(CalorieEntity entity) {
    return entityTemplate.insert(entity);
  }

  public Flux<CalorieEntity> findByUserId(String userId) {
    return entityTemplate.select(
        query(where("userId").is(userId)), CalorieEntity.class
    );
  }

  public Mono<CalorieEntity> findByFoodId(String foodId) {
    return entityTemplate.selectOne(
        query(where("foodId").is(foodId)), CalorieEntity.class
    );
  }

  public Flux<CalorieEntity> findByMealId(String mealId) {
    return entityTemplate.select(
        query(where("mealId").is(mealId)), CalorieEntity.class
    );
  }

  public Flux<CustomCalorieEntity> findByIdCustom(String id) {
    return entityTemplate.select(
        query(where("id").is(id)), CustomCalorieEntity.class
    );
  }

  public Mono<CustomCalorieEntity> findByFoodIdCustom(String foodId) {
    return entityTemplate.selectOne(
        query(where("foodId").is(foodId)), CustomCalorieEntity.class
    );
  }

  public Mono<CustomCalorieEntity> saveCustom(CustomCalorieEntity entity) {
    return entityTemplate.insert(entity);
  }
}
