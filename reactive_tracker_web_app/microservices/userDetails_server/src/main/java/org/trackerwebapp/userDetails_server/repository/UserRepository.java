package org.trackerwebapp.userDetails_server.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.trackerwebapp.userDetails_server.domain.entity.UserEntity;


@Repository
public interface UserRepository extends R2dbcRepository<UserEntity, String> {

}
