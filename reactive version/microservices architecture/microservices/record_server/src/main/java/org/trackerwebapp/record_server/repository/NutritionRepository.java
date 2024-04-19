package org.trackerwebapp.record_server.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.trackerwebapp.record_server.domain.entity.NutritionEntity;
import reactor.core.publisher.Flux;

@Repository
public interface NutritionRepository extends R2dbcRepository<NutritionEntity, String> {

  Flux<NutritionEntity> findByUserId(String userId);
}
