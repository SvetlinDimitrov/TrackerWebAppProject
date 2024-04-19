package org.trackerwebapp.customFood_server.repository;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.trackerwebapp.customFood_server.domain.entity.FoodEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FoodRepository extends R2dbcRepository<FoodEntity, String> {

  @Query("SELECT * FROM custom_foods WHERE id = :id")
  Mono<FoodEntity> findById(String id);

  @Query("SELECT * FROM custom_foods WHERE id = :id AND user_id = :userId")
  Mono<FoodEntity> findByIdAndUserId(String id, String userId);

  @Modifying
  @Query("DELETE FROM custom_foods WHERE id = :id")
  Mono<Void> deleteById(String id);

  Flux<FoodEntity> findByUserId(String userId);
}
