package org.trackerwebapp.meal_server.repository;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.trackerwebapp.meal_server.domain.entity.FoodEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FoodRepository extends R2dbcRepository<FoodEntity, String> {

  @Query("SELECT * FROM inserted_foods WHERE id = :id")
  Mono<FoodEntity> findById(String id);

  @Query("SELECT * FROM inserted_foods WHERE id = :id AND meal_id = :mealId AND user_id = :userId")
  Mono<FoodEntity> findByIdAndMealIdAndUserId(String id , String mealId , String userId);

  @Modifying
  @Query("DELETE FROM inserted_foods WHERE id = :id")
  Mono<Void> deleteById(String id);

  Flux<FoodEntity> findAllByMealId (String mealId);
}
