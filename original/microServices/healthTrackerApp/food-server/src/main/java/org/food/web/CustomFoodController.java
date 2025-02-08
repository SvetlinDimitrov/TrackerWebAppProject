package org.food.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.food.features.custom.dto.CustomFilterCriteria;
import org.food.features.custom.dto.CustomFoodRequestCreate;
import org.food.features.custom.dto.CustomFoodView;
import org.food.features.custom.service.CustomFoodService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/food/custom")
@RequiredArgsConstructor
public class CustomFoodController {

  private final CustomFoodService foodService;

  @GetMapping
  public ResponseEntity<Page<CustomFoodView>> getAll(
      @RequestHeader(name = "X-ViewUser") String userToken,
      Pageable pageable,
      CustomFilterCriteria filterCriteria
  ) {
    return ResponseEntity.ok(foodService.getAll(userToken, pageable , filterCriteria));
  }

  @GetMapping(path = "/{id}")
  public ResponseEntity<CustomFoodView> getById(
      @RequestHeader(name = "X-ViewUser") String userToken, @PathVariable String id) {
    return ResponseEntity.ok(foodService.getById(id, userToken));
  }

  @PostMapping
  public ResponseEntity<CustomFoodView> create(
      @Valid @RequestBody CustomFoodRequestCreate createCustomFood,
      @RequestHeader(name = "X-ViewUser") String userToken) {
    return ResponseEntity.ok(foodService.create(createCustomFood, userToken));
  }

  @DeleteMapping(path = "{id}")
  public ResponseEntity<Void> delete(
      @RequestHeader(name = "X-ViewUser") String userToken,
      @PathVariable(name = "id") String id) {
    foodService.delete(id, userToken);
    return ResponseEntity.ok().build();
  }
}