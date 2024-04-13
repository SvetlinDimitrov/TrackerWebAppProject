package org.trackerwebapp.record_server.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.trackerwebapp.record_server.domain.entity.UserDetailsEntity;
import reactor.core.publisher.Mono;

@Repository
public interface UserDetailsRepository extends R2dbcRepository<UserDetailsEntity, String> {

  @Query("SELECT * FROM user_details WHERE id = :id AND user_id = :userId")
  Mono<UserDetailsEntity> findByIdAndUserId(String id, String userId);

}
