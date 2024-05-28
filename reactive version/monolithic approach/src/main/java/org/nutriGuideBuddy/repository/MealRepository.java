package org.nutriGuideBuddy.repository;

import lombok.RequiredArgsConstructor;
import org.nutriGuideBuddy.domain.entity.CalorieEntity;
import org.nutriGuideBuddy.domain.entity.MealEntity;
import org.nutriGuideBuddy.domain.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Repository;
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

  public Mono<Page<MealEntity>> findAllMealsByUserId(String userId, Pageable pageable) {
    return entityTemplate.select(MealEntity.class)
        .matching(query(where("userId").is(userId)))
        .all()
        .collectList()
        .map(list -> new PageImpl<>(list, pageable, list.size()));
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
