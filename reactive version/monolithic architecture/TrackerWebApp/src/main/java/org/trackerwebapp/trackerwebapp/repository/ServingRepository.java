package org.trackerwebapp.trackerwebapp.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import org.trackerwebapp.trackerwebapp.domain.entity.CustomServingEntity;
import org.trackerwebapp.trackerwebapp.domain.entity.ServingEntity;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class ServingRepository {

  private final R2dbcEntityTemplate entityTemplate;

  public Mono<ServingEntity> save(ServingEntity entity) {
    return entityTemplate.insert(entity);
  }

  public Mono<ServingEntity> findByFoodId(String foodId) {
    return entityTemplate.selectOne(
        query(where("foodId").is(foodId)), ServingEntity.class
    );
  }

  public Mono<CustomServingEntity> saveCustom(CustomServingEntity entity) {
    return entityTemplate.insert(entity);
  }


  public Mono<CustomServingEntity> findByFoodIdCustom(String foodId) {
    return entityTemplate.selectOne(
        query(where("foodId").is(foodId)), CustomServingEntity.class
    );
  }
}
