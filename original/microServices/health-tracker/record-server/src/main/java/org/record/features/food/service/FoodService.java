package org.record.features.food.service;

import java.util.UUID;
import org.example.domain.food.shared.FoodRequest;
import org.example.domain.food.shared.OwnedFoodView;

public interface FoodService {

  OwnedFoodView create(UUID mealId, FoodRequest dto, String userToken);

  void delete(UUID mealId, UUID foodId, String userToken);

  OwnedFoodView get(UUID foodId, UUID mealId, String userToken);

  OwnedFoodView update(UUID mealId, UUID foodId, FoodRequest dto, String userToken);
}