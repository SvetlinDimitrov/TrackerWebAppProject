package org.trackerwebapp.record_server.repository;

import java.math.BigDecimal;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.trackerwebapp.record_server.domain.entity.RecordEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RecordRepository extends R2dbcRepository<RecordEntity, String> {

  @Query("SELECT * FROM records WHERE id = :id AND user_id = :userId")
  Mono<RecordEntity> findByIdAndUserId(String id, String userId);

  @Query("SELECT * FROM records WHERE user_id = :userId")
  Flux<RecordEntity> findAllByUserId(String userId);

  @Modifying
  @Query("DELETE FROM records WHERE id = :id AND user_id = :userId")
  Mono<Void> deleteByIdAndUserId(String id, String userId);

  @Modifying
  @Query("UPDATE records SET name = :name, daily_calories = :dailyCalories WHERE id = :id AND user_id = :userId")
  Mono<Void> updateRecordByIdAndUserId(String name, BigDecimal dailyCalories, String id, String userId);
}
