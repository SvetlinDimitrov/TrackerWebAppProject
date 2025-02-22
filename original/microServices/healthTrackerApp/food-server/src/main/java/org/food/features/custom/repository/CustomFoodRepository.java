package org.food.features.custom.repository;

import java.util.UUID;
import org.food.features.custom.entity.CustomFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomFoodRepository extends JpaRepository<CustomFood, UUID>,
    JpaSpecificationExecutor<CustomFood> {

  void deleteAllByUserId(UUID userId);
}