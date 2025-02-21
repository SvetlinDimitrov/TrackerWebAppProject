package org.food.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.food.features.embedded.service.EmbeddedFoodService;
import org.example.domain.food.embedded.dto.EmbeddedFilterCriteria;
import org.example.domain.food.shared.dto.FoodView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/food/embedded/{foodType}")
@RequiredArgsConstructor
@Validated
public class EmbeddedFoodController {

  private final EmbeddedFoodService service;

  @GetMapping
  public <T extends FoodView> ResponseEntity<Page<T>> getAll(
      Pageable pageable,
      @Valid EmbeddedFilterCriteria filterCriteria,
      @PathVariable String foodType
  ) {
    return ResponseEntity.ok(service.getAll(pageable, filterCriteria, foodType));
  }

  @GetMapping(path = "/{id}")
  public <T extends FoodView> ResponseEntity<T> getById(
      @PathVariable String id,
      @PathVariable String foodType
  ) {
    return ResponseEntity.ok(service.getById(id, foodType));
  }
}
