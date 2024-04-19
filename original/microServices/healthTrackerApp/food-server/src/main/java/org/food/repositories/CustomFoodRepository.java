package org.food.repositories;

import java.util.List;
import java.util.Optional;

import org.food.domain.dtos.foodView.NotCompleteFoodView;
import org.food.domain.entity.CustomFoodEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface CustomFoodRepository extends MongoRepository<CustomFoodEntity, String> {
    @Query(value = "{'userId' : ?0}", fields = "{ 'description' : 1 , 'foodClass' : 1}")
    List<NotCompleteFoodView> findAllProjectedByDescriptionAndNameByUserId(String userId);

    @Query(value = "{ 'description': {$regex: ?0, $options: 'i'}, 'userId' : ?1 }", fields = "{ 'description' : 1 , 'foodClass' : 1}")
    List<NotCompleteFoodView> findAllProjectedByRegex(String regexWord , String userId);

    Optional<CustomFoodEntity> findByDescriptionAndUserId (String name , String userId);
    Optional<CustomFoodEntity> findByIdAndUserId (String id , String userId);
    void deleteAllByUserId(String userId);

}