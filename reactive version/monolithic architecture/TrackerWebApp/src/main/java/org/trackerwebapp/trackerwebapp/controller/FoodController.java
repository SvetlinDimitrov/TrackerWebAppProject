package org.trackerwebapp.trackerwebapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.trackerwebapp.trackerwebapp.config.security.UserPrincipal;
import org.trackerwebapp.trackerwebapp.domain.dto.BadRequestException;
import org.trackerwebapp.trackerwebapp.domain.dto.ExceptionResponse;
import org.trackerwebapp.trackerwebapp.domain.dto.meal.InsertFoodDto;
import org.trackerwebapp.trackerwebapp.service.FoodServiceImp;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meals/{mealId}/insertFood")
public class FoodController {

  private final FoodServiceImp service;

  @PostMapping
  private Mono<Void> addFood(@AuthenticationPrincipal UserPrincipal user, @RequestBody InsertFoodDto dto, @PathVariable String mealId) {
    return service.addFoodToMeal(user.getId(), dto, mealId);
  }

  @DeleteMapping("/{foodId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  private Mono<Void> deleteFoodFromMeal(@AuthenticationPrincipal UserPrincipal user, @PathVariable String mealId, @PathVariable String foodId) {
    return service.deleteFoodById(user.getId(), mealId, foodId);
  }

  @PutMapping("/{foodId}")
  private Mono<Void> changeFood(@AuthenticationPrincipal UserPrincipal user, @PathVariable String mealId, @PathVariable String foodId, @RequestBody InsertFoodDto dto) {
    return service.changeFood(user.getId(), mealId, foodId, dto);
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ExceptionResponse> catchUserNotFound(BadRequestException e) {
    return Mono.just(new ExceptionResponse(e.getMessage()));
  }
}
