package org.trackerwebapp.user_server.repository;

import java.util.UUID;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.trackerwebapp.user_server.domain.entity.UserEntity;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<UserEntity, String> {

  Mono<UserEntity> findByEmail(String email);
}
