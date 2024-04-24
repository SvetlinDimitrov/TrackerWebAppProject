package org.trackerwebapp.trackerwebapp.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Repository;
import org.trackerwebapp.trackerwebapp.domain.entity.UserDetails;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class UserDetailsRepository {

  private final R2dbcEntityTemplate entityTemplate;

  public Mono<UserDetails> save(UserDetails entity) {
    return entityTemplate.insert(entity);
  }

  public Mono<UserDetails> findByUserId(String userId) {
    return entityTemplate.selectOne(
        query(where("userId").is(userId)), UserDetails.class
    );
  }

  public Mono<UserDetails> findById(String id) {
    return entityTemplate.selectOne(
        query(where("id").is(id)), UserDetails.class
    );
  }

  @Modifying
  public Mono<UserDetails> update(String id, UserDetails updatedEntity) {
    return entityTemplate.update(UserDetails.class)
        .matching(query(where("id").is(id)))
        .apply(
            Update.update("kilograms", updatedEntity.getKilograms())
                .set("height", updatedEntity.getHeight())
                .set("age", updatedEntity.getAge())
                .set("workoutState", updatedEntity.getWorkoutState())
                .set("gender", updatedEntity.getGender())
        )
        .then(findById(id));
  }
}
