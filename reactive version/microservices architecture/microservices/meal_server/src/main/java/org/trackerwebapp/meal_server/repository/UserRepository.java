package org.trackerwebapp.meal_server.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.trackerwebapp.meal_server.domain.entity.UserEntity;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<UserEntity, String> {

  @Override
  @Query("SELECT * FROM users WHERE id = :id")
  Mono<UserEntity> findById(String id);
}
