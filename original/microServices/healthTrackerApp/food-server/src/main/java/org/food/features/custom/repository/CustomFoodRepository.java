package org.food.features.custom.repository;

import java.util.Optional;
import org.food.features.custom.dto.CustomFilterCriteria;
import org.food.features.custom.dto.CustomFoodView;
import org.food.features.custom.entity.CustomFoodEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomFoodRepository extends MongoRepository<CustomFoodEntity, String> {

    @Query(value = "{ 'userId' : ?0, 'description': ?#{[2].description != null ? {'$regex': [2].description, '$options': 'i'} : {'$exists': true }} }")
    Page<CustomFoodView> findAllByUserId(
        String userId,
        Pageable pageable,
        CustomFilterCriteria filterCriteria
    );

    Optional<CustomFoodEntity> findByIdAndUserId (String id , String userId);

    void deleteAllByUserId(String userId);
}