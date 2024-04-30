package org.trackerwebapp.trackerwebapp.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import org.trackerwebapp.trackerwebapp.domain.entity.CalorieEntity;
import org.trackerwebapp.trackerwebapp.domain.entity.NutritionEntity;
import org.trackerwebapp.trackerwebapp.domain.entity.UserDetails;
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
