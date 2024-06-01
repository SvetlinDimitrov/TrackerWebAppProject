package org.nutriGuideBuddy.repository;

import lombok.RequiredArgsConstructor;
import org.nutriGuideBuddy.domain.entity.CalorieEntity;
import org.nutriGuideBuddy.domain.entity.NutritionEntity;
import org.nutriGuideBuddy.domain.entity.UserDetails;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class RecordRepository {

  private final R2dbcEntityTemplate entityTemplate;

  public Flux<CalorieEntity> findCalorieByUserId(String userId) {
    return entityTemplate.select(
        query(where("userId").is(userId)), CalorieEntity.class
    );
  }

  public Flux<NutritionEntity> findAllNutritionsByUserId(String userId) {
    return entityTemplate.select(
        query(where("userId").is(userId)), NutritionEntity.class
    );
  }

  public Mono<UserDetails> findUserDetailsByUserId(String userId) {
    return entityTemplate.selectOne(
        query(where("userId").is(userId)), UserDetails.class
    );
  }
}
