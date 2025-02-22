package org.record.features.food.repository;

import java.util.Optional;
import java.util.UUID;
import org.record.features.food.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, UUID> {

  @Query("SELECT f FROM Food f WHERE f.id = :foodId "
      + "AND f.meal.id = :mealId "
      + "AND f.userId = :userId")
  Optional<Food> findByIdAndMealIdAndUserId(@Param("foodId") UUID foodId,
      @Param("mealId") UUID mealId,
      @Param("userId") UUID userId);

  @Query("SELECT CASE WHEN COUNT(f) > 0 "
      + "THEN TRUE ELSE FALSE END "
      + "FROM Food f WHERE f.id = :foodId "
      + "AND f.meal.id = :mealId "
      + "AND f.userId = :userId")
  boolean existsByIdAndMealIdAndUserId(@Param("foodId") UUID foodId,
      @Param("mealId") UUID mealId,
      @Param("userId") UUID userId);
}