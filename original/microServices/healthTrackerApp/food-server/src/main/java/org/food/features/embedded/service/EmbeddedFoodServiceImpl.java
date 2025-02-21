package org.food.features.embedded.service;

import static org.food.infrastructure.exception.ExceptionMessages.FOOD_NOT_FOUND_WITH_ID;
import static org.food.infrastructure.exception.ExceptionMessages.INVALID_FOOD_TYPE;

import lombok.RequiredArgsConstructor;
import org.example.exceptions.throwable.BadRequestException;
import org.example.exceptions.throwable.NotFoundException;
import org.example.domain.food.embedded.dto.BrandedFoodView;
import org.example.domain.food.embedded.dto.FinalFoodView;
import org.example.domain.food.embedded.dto.SurveyFoodView;
import org.food.features.embedded.repository.EmbeddedFoodRepository;
import org.example.domain.food.embedded.dto.EmbeddedFilterCriteria;
import org.example.domain.food.shared.dto.FoodView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmbeddedFoodServiceImpl implements EmbeddedFoodService {

  private final EmbeddedFoodRepository repository;

  @SuppressWarnings("unchecked")
  public <T extends FoodView> Page<T> getAll(
      Pageable pageable,
      EmbeddedFilterCriteria filter,
      String foodType
  ) {
    return switch (foodType) {
      case "branded" -> (Page<T>) repository
          .findAll("Branded", pageable, filter, BrandedFoodView.class);
      case "survey" -> (Page<T>) repository
          .findAll("Survey", pageable, filter, SurveyFoodView.class);
      case "final" -> (Page<T>) repository
          .findAll("FinalFood", pageable, filter, FinalFoodView.class);
      default -> throw new BadRequestException(INVALID_FOOD_TYPE);
    };
  }

  @SuppressWarnings("unchecked")
  public <T extends FoodView> T getById(String id, String foodType) {
    return switch (foodType) {
      case "branded" -> (T)
          repository.findById(id, "Branded", BrandedFoodView.class)
              .orElseThrow(() -> new NotFoundException(FOOD_NOT_FOUND_WITH_ID, id));
      case "survey" -> (T)
          repository.findById(id, "Survey", SurveyFoodView.class)
              .orElseThrow(() -> new NotFoundException(FOOD_NOT_FOUND_WITH_ID, id));
      case "final" -> (T)
          repository.findById(id, "FinalFood", FinalFoodView.class)
              .orElseThrow(() -> new NotFoundException(FOOD_NOT_FOUND_WITH_ID, id));
      default -> throw new BadRequestException(INVALID_FOOD_TYPE);
    };
  }
}
