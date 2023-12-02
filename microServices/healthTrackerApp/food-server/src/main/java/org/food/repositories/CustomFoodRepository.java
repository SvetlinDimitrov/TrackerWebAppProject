package org.food.repositories;

import java.util.Optional;

import org.food.domain.entity.CustomFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface CustomFoodRepository extends JpaRepository<CustomFood, Long>{
    Optional<CustomFood> findByIdAndUserId(Long id, Long userId);
    Optional<CustomFood> findByNameAndUserId (String name , Long userId);
    void deleteAllByUserId(Long userId);

}