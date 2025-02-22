package org.food.features.custom.service;

import java.util.UUID;
import org.example.domain.food.shared.FoodCreateRequest;
import org.example.domain.food.shared.FoodUpdateRequest;
import org.example.domain.food.shared.FoodView;
import org.food.features.custom.dto.CustomFilterCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomFoodService {

  Page<FoodView> getAll(String userToken, Pageable pageable , CustomFilterCriteria filterCriteria);

  FoodView getById(UUID id, String userToken);

  FoodView create(FoodCreateRequest dto, String userToken);

  void delete(UUID id, String userToken);
}
