package org.trackerwebapp.user_server.repository;

import org.springframework.data.r2dbc.core.ReactiveSelectOperation.SelectWithQuery;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.trackerwebapp.user_server.domain.entity.UserEntity;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<UserEntity, String> {

  @Override
  @Query("SELECT * FROM users WHERE id = :id")
  Mono<UserEntity> findById(String id);

  Mono<UserEntity> findByEmail(String email);

  @Modifying
  @Query("UPDATE users SET username = :username WHERE id = :id")
  Mono<Void> updateAndFetch(String id, String username);

  @Override
  @Modifying
  @Query("DELETE FROM users WHERE id = :id")
  Mono<Void> deleteById(String id);
}
