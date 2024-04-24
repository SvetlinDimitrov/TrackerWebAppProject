package org.trackerwebapp.trackerwebapp.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.trackerwebapp.trackerwebapp.domain.dto.user.UserWithDetails;
import org.trackerwebapp.trackerwebapp.domain.entity.UserDetails;
import org.trackerwebapp.trackerwebapp.domain.entity.UserEntity;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class UserRepository {

  private final R2dbcEntityTemplate entityTemplate;

  public Mono<UserEntity> save(UserEntity entity) {
    return entityTemplate.insert(entity);
  }

  public Mono<UserEntity> findById(String id) {
    return entityTemplate.selectOne(
        query(where("id").is(id)), UserEntity.class
    );
  }

  public Mono<UserEntity> findByEmail(String email) {
    return entityTemplate.selectOne(
        query(where("email").is(email)), UserEntity.class
    );
  }

  @Modifying
  public Mono<Void> deleteById(String id) {
    return entityTemplate.delete(UserEntity.class)
        .matching(
            query(where("id").is(id))
        ).all()
        .then();
  }

  @Modifying
  public Mono<UserEntity> updateUsernameAndPassword(String id, UserEntity updatedEntity) {
    return entityTemplate.update(UserEntity.class)
        .matching(query(where("id").is(id)))
        .apply(
            Update.update("username", updatedEntity.getUsername())
                .set("password", updatedEntity.getPassword())
        )
        .then(findById(id));
  }

  @Transactional(readOnly = true)
  public Mono<UserWithDetails> getUserWithDetailsByEmail(String email) {
    return entityTemplate.select(UserEntity.class)
        .matching(Query.query(Criteria.where("email").is(email)))
        .one()
        .flatMap(userEntity ->
            entityTemplate.select(UserDetails.class)
                .matching(Query.query(Criteria.where("userId").is(userEntity.getId())))
                .one()
                .map(userDetails -> new UserWithDetails(userEntity, userDetails))
        );
  }
}
