package org.food.features.custom.service;

import java.util.UUID;
import org.example.domain.food.shared.FoodRequest;
import org.example.domain.food.shared.OwnedFoodView;
import org.food.features.custom.dto.CustomFilterCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomFoodService {

  Page<OwnedFoodView> getAll(String userToken, Pageable pageable,
      CustomFilterCriteria filterCriteria);

  OwnedFoodView getById(UUID id, String userToken);

  OwnedFoodView create(FoodRequest dto, String userToken);

  void delete(UUID id, String userToken);

  OwnedFoodView update(UUID id, FoodRequest request, String userToken);
}
