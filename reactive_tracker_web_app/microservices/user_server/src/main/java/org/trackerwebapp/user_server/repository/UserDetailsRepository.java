package org.trackerwebapp.user_server.repository;

import java.math.BigDecimal;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.trackerwebapp.user_server.domain.entity.UserDetails;
import reactor.core.publisher.Mono;

@Repository
public interface UserDetailsRepository extends R2dbcRepository<UserDetails, String> {

  @Query("SELECT * FROM user_details WHERE user_id = :userId")
  Mono<UserDetails> findByUserId(String userId);

  @Query("SELECT * FROM user_details WHERE id = :id AND user_id = :userId")
  Mono<UserDetails> findByIdAndUserId(String id, String userId);

  @Modifying
  @Query("DELETE FROM user_details WHERE id = :id AND user_id = :userId")
  Mono<Void> deleteByIdAndUserId(String id, String userId);

  @Modifying
  @Query("UPDATE user_details SET kilograms = :kilograms, height = :height, age = :age, workout_state = :workoutState, gender = :gender , user_id = :userId WHERE id = :id")
  Mono<Void> updateAndFetch(String id, BigDecimal kilograms, BigDecimal height, Integer age,
      String workoutState, String gender, String userId);
}
