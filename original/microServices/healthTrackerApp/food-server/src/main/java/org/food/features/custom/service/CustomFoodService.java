package org.food.features.custom.service;

import org.example.domain.food.custom.dto.CustomFilterCriteria;
import org.example.domain.food.custom.dto.CustomFoodRequestCreate;
import org.example.domain.food.custom.dto.CustomFoodView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomFoodService {

  Page<CustomFoodView> getAll(String userToken, Pageable pageable , CustomFilterCriteria filterCriteria);

  CustomFoodView getById(String id, String userToken);

  CustomFoodView create(CustomFoodRequestCreate dto, String userToken);

  void delete(String id, String userToken);
}
