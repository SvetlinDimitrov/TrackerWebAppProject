package org.food.web;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.domain.food.shared.FoodView;
import org.example.domain.food.nutriox_api.ListFoodsResponse;
import org.food.infrastructure.nutritionix.NutritionixApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/food/search")
public class NutritionixApiController {

  private final NutritionixApiService service;

  @GetMapping("/common/{term}")
  public ResponseEntity<List<FoodView>> getFoodBySearchCriteria(@PathVariable String term) {
    return ResponseEntity.ok(service.getCommonFoodBySearchTerm(term));
  }

  @GetMapping("/branded/{id}")
  public ResponseEntity<FoodView> getBrandedFoodById(@PathVariable String id) {
    return ResponseEntity.ok(service.getBrandedFoodById(id));
  }

  @GetMapping
  public ResponseEntity<ListFoodsResponse> getFoodsByName(@RequestParam String foodName) {
    return ResponseEntity.ok(service.getAllFoodsByFoodName(foodName));
  }
}
