package org.food.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import lombok.RequiredArgsConstructor;
import org.example.domain.food.shared.dto.FoodView;
import org.food.infrastructure.utils.FoodUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/food")
@RequiredArgsConstructor
public class BaseFoodController {

  private final FoodUtils foodUtils;

  @PatchMapping("/calculate")
  public ResponseEntity<FoodView> calculateFood(
      @RequestBody @Valid FoodView food,
      @RequestParam(name = "amount") @DecimalMin("0.0") Double amount) {
    foodUtils.calculateFoodByAmount(food, amount);
    return ResponseEntity.ok(food);
  }
}
