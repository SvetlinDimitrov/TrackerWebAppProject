package org.trackerwebapp.record_server.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.trackerwebapp.record_server.domain.entity.CalorieEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CalorieRepository extends R2dbcRepository<CalorieEntity, String> {

  Flux<CalorieEntity> findByUserId(String userId);

}
