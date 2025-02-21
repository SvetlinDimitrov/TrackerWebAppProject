package org.food.features.custom.repository;

import java.util.Optional;
import java.util.UUID;
import org.example.domain.food.custom.dto.CustomFilterCriteria;
import org.example.domain.food.custom.dto.CustomFoodView;
import org.example.domain.food.custom.entity.CustomFoodEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomFoodRepository extends MongoRepository<CustomFoodEntity, String> {

    @Query(value = "{ 'userId' : ?0, 'description': ?#{[2].description != null ? {'$regex': [2].description, '$options': 'i'} : {'$exists': true }} }")
    Page<CustomFoodView> findAllByUserId(
        UUID userId,
        Pageable pageable,
        CustomFilterCriteria filterCriteria
    );

    Optional<CustomFoodEntity> findByIdAndUserId (String id , UUID userId);

    void deleteAllByUserId(UUID userId);
}