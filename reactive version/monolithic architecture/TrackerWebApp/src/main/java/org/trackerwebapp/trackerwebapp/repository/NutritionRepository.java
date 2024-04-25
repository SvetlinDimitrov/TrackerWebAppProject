package org.trackerwebapp.trackerwebapp.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import org.trackerwebapp.trackerwebapp.domain.entity.CustomNutritionEntity;
import org.trackerwebapp.trackerwebapp.domain.entity.NutritionEntity;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class NutritionRepository {

  private final R2dbcEntityTemplate entityTemplate;

  public Flux<NutritionEntity> saveAll(List<NutritionEntity> entities) {
    return Flux.fromIterable(entities)
        .flatMapSequential(entityTemplate::insert);
  }

  public Flux<NutritionEntity> findByUserId(String userId) {
    return entityTemplate.select(
        query(where("userId").is(userId)), NutritionEntity.class
    );
  }

  public Flux<NutritionEntity> findAllByFoodId(String foodId) {
    return entityTemplate.select(
        query(where("foodId").is(foodId)), NutritionEntity.class
    );
  }

  public Flux<CustomNutritionEntity> findAllByFoodIdCustom(String foodId) {
    return entityTemplate.select(
        query(where("foodId").is(foodId)), CustomNutritionEntity.class
    );
  }

  public Flux<CustomNutritionEntity> saveAllCustom(List<CustomNutritionEntity> entities) {
    return Flux.fromIterable(entities)
        .flatMapSequential(entityTemplate::insert);
  }
}
