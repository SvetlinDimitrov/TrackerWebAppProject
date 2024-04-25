package org.trackerwebapp.trackerwebapp.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.trackerwebapp.trackerwebapp.domain.entity.CustomFoodEntity;
import org.trackerwebapp.trackerwebapp.domain.entity.FoodEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class FoodRepository {

  private final R2dbcEntityTemplate entityTemplate;

  public Mono<FoodEntity> save(FoodEntity entity) {
    return entityTemplate.insert(entity);
  }

  public Mono<FoodEntity> findById(String id) {
    return entityTemplate.selectOne(
        query(where("id").is(id)), FoodEntity.class
    );
  }

  public Mono<FoodEntity> findByIdAndMealIdAndUserId(String id, String mealId, String userId) {
    return entityTemplate.selectOne(
        query(where("id").is(id)
            .and("mealId").is(mealId)
            .and("userId").is(userId)
        ), FoodEntity.class
    );
  }

  @Modifying
  public Mono<Void> deleteById(String id) {
    return entityTemplate.delete(FoodEntity.class)
        .matching(
            query(where("id").is(id))
        ).all()
        .then();
  }

  public Flux<FoodEntity> findAllByMealId(String mealId) {
    return entityTemplate.select(
        query(where("mealId").is(mealId)), FoodEntity.class
    );
  }

  public Mono<CustomFoodEntity> findByIdCustom(String id) {
    return entityTemplate.selectOne(
        query(where("id").is(id)), CustomFoodEntity.class
    );
  }

  public Mono<CustomFoodEntity> findByIdAndUserIdCustom(String id, String userId) {
    return entityTemplate.selectOne(
        query(
            where("id").is(id)
                .and("userId").is(userId)
        ), CustomFoodEntity.class
    );
  }

  public Flux<CustomFoodEntity> findAllByUserIdCustom(String userId) {
    return entityTemplate.select(
        query(where("userId").is(userId)), CustomFoodEntity.class
    );
  }

  @Modifying
  public Mono<Void> deleteByIdCustom(String id) {
    return entityTemplate.delete(CustomFoodEntity.class)
        .matching(
            query(where("id").is(id))
        ).all()
        .then();
  }

  public Mono<CustomFoodEntity> saveCustom(CustomFoodEntity entity) {
    return entityTemplate.insert(entity);
  }

  public Mono<CustomFoodEntity> findByNameCustom(String name) {
    return entityTemplate.selectOne(
        query(where("name").is(name)), CustomFoodEntity.class
    );
  }

}
