package org.food.features.embedded.service;

import org.food.features.embedded.dto.EmbeddedFilterCriteria;
import org.food.features.shared.dto.FoodView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmbeddedFoodService {

  <T extends FoodView> Page<T> getAll(
      Pageable pageable,
      EmbeddedFilterCriteria filter,
      String foodType
  );

  <T extends FoodView> T getById(String id, String foodType);
}
