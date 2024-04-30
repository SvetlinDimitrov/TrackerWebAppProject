package org.trackerwebapp.trackerwebapp.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Repository;
import org.trackerwebapp.trackerwebapp.domain.entity.CalorieEntity;
import org.trackerwebapp.trackerwebapp.domain.entity.MealEntity;
import org.trackerwebapp.trackerwebapp.domain.entity.UserEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class MealRepository {

  private final R2dbcEntityTemplate entityTemplate;


  public Mono<MealEntity> saveMeal(MealEntity entity) {
    return entityTemplate.insert(entity);
  }

  public Flux<MealEntity> findAllMealsByUserId(String userId) {
    return entityTemplate.select(
        query(where("userId").is(userId)), MealEntity.class
    );
  }

  public Mono<MealEntity> findMealByIdAndUserId(String id, String userId) {
    return entityTemplate.selectOne(
        query(where("userId").is(userId)
            .and("id").is(id)
        ), MealEntity.class
    );
  }

  @Modifying
  public Mono<Void> deleteMealById(String id) {
    return entityTemplate.delete(MealEntity.class)
        .matching(
            query(where("id").is(id))
        ).all()
        .then();
  }

  @Modifying
  public Mono<Void> updateMealNameByIdAndUserId(String id, String userId, MealEntity updatedEntity) {
    return entityTemplate.update(MealEntity.class)
        .matching(query(where("id").is(id)
            .and("userId").is(userId))
        )
        .apply(
            Update.update("name", updatedEntity.getName())
        )
        .then();
  }

  public Flux<CalorieEntity> findCalorieByMealId(String mealId) {
    return entityTemplate.select(
        query(where("mealId").is(mealId)), CalorieEntity.class
    );
  }

  public Mono<UserEntity> findUserById(String id) {
    return entityTemplate.selectOne(
        query(where("id").is(id)), UserEntity.class
    );
  }

}
