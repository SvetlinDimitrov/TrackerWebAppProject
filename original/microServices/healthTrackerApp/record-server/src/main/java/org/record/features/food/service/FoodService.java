package org.record.features.food.service;

import java.util.UUID;
import org.example.domain.food.shared.FoodCreateRequest;
import org.example.domain.food.shared.FoodView;

public interface FoodService {

  FoodView create(UUID mealId, FoodCreateRequest dto, String userToken);

  void delete(UUID mealId, UUID foodId, String userToken);

  FoodView get(UUID foodId, UUID mealId, String userToken);

  FoodView update(UUID mealId, UUID foodId, FoodCreateRequest dto, String userToken);
}