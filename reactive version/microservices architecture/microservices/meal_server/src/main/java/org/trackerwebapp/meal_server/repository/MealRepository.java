package org.trackerwebapp.meal_server.repository;

import java.math.BigDecimal;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.trackerwebapp.meal_server.domain.entity.MealEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MealRepository extends R2dbcRepository<MealEntity, String> {

  Flux<MealEntity> findAllByUserId(String userId);

  @Query("SELECT * FROM meals WHERE id = :id AND user_id = :userId")
  Mono<MealEntity> findByIdAndUserId(String id, String userId);

  @Modifying
  @Query("UPDATE meals SET name = :name WHERE id = :id AND user_id = :userId")
  Mono<Void> updateMealNameByIdAndUserId(String name, String id, String userId);

  @Modifying
  @Query("UPDATE meals SET consumed_calories = :consumption WHERE id = :id AND user_id = :userId")
  Mono<Void> updateMealDailyConsumptionByIdAndUserId(BigDecimal consumption, String id, String userId);

  @Modifying
  @Query("DELETE FROM meals WHERE id = :id")
  Mono<Void> deleteById(String id);
}
