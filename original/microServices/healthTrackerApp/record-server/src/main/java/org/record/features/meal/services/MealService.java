package org.record.features.meal.services;

import java.util.UUID;
import org.record.features.meal.dto.MealRequest;
import org.record.features.meal.dto.MealView;
import org.record.features.meal.entity.Meal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MealService {

  Page<MealView> getAll(UUID recordId, String userToken, Pageable pageable);

  MealView getById(UUID mealId, String userToken);

  MealView create(UUID recordId, MealRequest dto, String userToken);

  MealView update(UUID mealId, MealRequest dto, String userToken);

  void delete(UUID mealId, String userToken);

  Meal findByIdAndUserId(UUID storageId, UUID userId);
}