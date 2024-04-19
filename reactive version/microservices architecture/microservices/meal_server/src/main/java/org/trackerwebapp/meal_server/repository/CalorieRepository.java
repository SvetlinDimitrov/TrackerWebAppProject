package org.trackerwebapp.meal_server.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.trackerwebapp.meal_server.domain.entity.CalorieEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CalorieRepository extends R2dbcRepository<CalorieEntity, String> {

  @Query("SELECT * FROM calories WHERE id = :id")
  Mono<CalorieEntity> findById(String id);

  @Query("SELECT * FROM calories WHERE meal_id = :mealId")
  Flux<CalorieEntity> findByMealId(String mealId);

  @Query("SELECT * FROM calories WHERE food_id = :foodId")
  Mono<CalorieEntity> findByFoodId(String foodId);

}
