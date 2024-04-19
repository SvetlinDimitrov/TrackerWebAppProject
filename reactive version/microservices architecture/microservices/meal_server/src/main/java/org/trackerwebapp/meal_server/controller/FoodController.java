package org.trackerwebapp.meal_server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.trackerwebapp.meal_server.domain.dto.InsertFoodDto;
import org.trackerwebapp.meal_server.service.FoodService;
import org.trackerwebapp.shared_interfaces.domain.dto.ExceptionResponse;
import org.trackerwebapp.shared_interfaces.domain.exception.BadRequestException;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meals/{mealId}/insertFood")
public class FoodController {

  private final FoodService service;

  @PostMapping
  private Mono<Void> addFood(
      @AuthenticationPrincipal User user,
      @RequestBody InsertFoodDto dto,
      @PathVariable String mealId) {
    return service.addFoodToMeal(user.getUsername(), dto, mealId);
  }

  @DeleteMapping("/{foodId}")
  private Mono<Void> deleteFoodFromMeal(
      @AuthenticationPrincipal User user,
      @PathVariable String mealId,
      @PathVariable String foodId) {
    return service.deleteFoodById(user.getUsername(), mealId, foodId);
  }

  @PatchMapping("/{foodId}")
  private Mono<Void> changeFood(
      @AuthenticationPrincipal User user,
      @PathVariable String mealId,
      @PathVariable String foodId,
      @RequestBody InsertFoodDto dto) {
    return service.changeFood(user.getUsername(), mealId, foodId, dto);
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ExceptionResponse> catchUserNotFound(BadRequestException e) {
    return Mono.just(new ExceptionResponse(e.getMessage()));
  }
}
