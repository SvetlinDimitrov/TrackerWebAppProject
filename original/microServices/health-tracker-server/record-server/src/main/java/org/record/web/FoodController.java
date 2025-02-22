package org.record.web;

import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.example.domain.food.shared.FoodCreateRequest;
import org.example.domain.food.shared.FoodView;
import org.record.features.food.service.FoodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/meal/{mealId}/food")
@RequiredArgsConstructor
public class FoodController {

  private final FoodService service;

  @GetMapping("/{foodId}")
  public ResponseEntity<FoodView> get(
      @PathVariable UUID mealId, @PathVariable UUID foodId,
      @RequestHeader("X-ViewUser") String userToken) {
    return new ResponseEntity<>(
        service.get(foodId, mealId, userToken),
        HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<FoodView> create(
      @PathVariable UUID mealId, @RequestBody @Valid FoodCreateRequest dto,
      @RequestHeader("X-ViewUser") String userToken) {
    return new ResponseEntity<>(
        service.create(mealId, dto, userToken),
        HttpStatus.CREATED);
  }

  @PatchMapping("/{foodId}")
  public ResponseEntity<FoodView> update(
      @PathVariable UUID mealId, @PathVariable UUID foodId,
      @RequestBody @Valid FoodCreateRequest dto,
      @RequestHeader("X-ViewUser") String userToken) {
    return new ResponseEntity<>(
        service.update(mealId, foodId, dto, userToken),
        HttpStatus.OK);
  }

  @DeleteMapping("/{foodId}")
  public ResponseEntity<Void> delete(
      @PathVariable UUID mealId, @PathVariable UUID foodId,
      @RequestHeader("X-ViewUser") String userToken) {
    service.delete(mealId, foodId, userToken);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
