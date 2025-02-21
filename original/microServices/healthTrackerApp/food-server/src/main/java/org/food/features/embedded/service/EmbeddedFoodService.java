package org.food.features.embedded.service;

import org.example.domain.food.embedded.dto.EmbeddedFilterCriteria;
import org.example.domain.food.shared.dto.FoodView;
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
