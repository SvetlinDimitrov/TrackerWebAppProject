package org.nutriGuideBuddy.controller;

import lombok.RequiredArgsConstructor;
import org.nutriGuideBuddy.config.security.UserPrincipal;
import org.nutriGuideBuddy.domain.dto.BadRequestException;
import org.nutriGuideBuddy.domain.dto.ExceptionResponse;
import org.nutriGuideBuddy.domain.dto.meal.CreateMeal;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.nutriGuideBuddy.domain.dto.meal.MealView;
import org.nutriGuideBuddy.service.MealService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meals")
public class MealController {

  private final MealService service;

  @GetMapping
  private Flux<MealView> getAllMealsByUserId(@AuthenticationPrincipal UserPrincipal user) {
    return service.getAllByUserId(user.getId());
  }

  @GetMapping("/{mealId}")
  private Mono<MealView> getMealById(@AuthenticationPrincipal UserPrincipal user, @PathVariable String mealId) {
    return service.getByIdAndUserId(mealId, user.getId());
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  private Mono<MealView> createMeal(@AuthenticationPrincipal UserPrincipal user, @RequestBody CreateMeal dto) {
    return service.createMeal(user.getId(), dto);
  }

  @PatchMapping("/{mealId}")
  private Mono<MealView> changeMeal(@AuthenticationPrincipal UserPrincipal user, @RequestBody CreateMeal dto, @PathVariable String mealId) {
    return service.modifyMeal(user.getId(), dto, mealId);
  }

  @DeleteMapping("/{mealId}")
  private Mono<Void> deleteMealById(@AuthenticationPrincipal UserPrincipal user, @PathVariable String mealId) {
    return service.deleteByIdAndUserId(mealId, user.getId());
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ExceptionResponse> catchUserNotFound(BadRequestException e) {
    return Mono.just(new ExceptionResponse(e.getMessage()));
  }
}
