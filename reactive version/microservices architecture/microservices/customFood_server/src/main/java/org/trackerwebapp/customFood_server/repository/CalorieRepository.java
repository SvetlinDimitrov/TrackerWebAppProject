package org.trackerwebapp.customFood_server.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.trackerwebapp.customFood_server.domain.entity.CalorieEntity;
import reactor.core.publisher.Mono;

@Repository
public interface CalorieRepository extends R2dbcRepository<CalorieEntity, String> {

  @Query("SELECT * FROM custom_foods_calories WHERE id = :id")
  Mono<CalorieEntity> findById(String id);

  @Query("SELECT * FROM custom_foods_calories WHERE food_id = :foodId")
  Mono<CalorieEntity> findByFoodId(String foodId);

}
