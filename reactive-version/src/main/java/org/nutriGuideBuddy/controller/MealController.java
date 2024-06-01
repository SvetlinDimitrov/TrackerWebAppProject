package org.nutriGuideBuddy.controller;

import lombok.RequiredArgsConstructor;
import org.nutriGuideBuddy.domain.dto.BadRequestException;
import org.nutriGuideBuddy.domain.dto.ExceptionResponse;
import org.nutriGuideBuddy.domain.dto.meal.CreateMeal;
import org.nutriGuideBuddy.domain.dto.meal.MealShortView;
import org.nutriGuideBuddy.domain.dto.meal.MealView;
import org.nutriGuideBuddy.service.MealService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meals")
public class MealController {

  private final MealService service;

  @GetMapping
  private Mono<Page<MealView>> getAllMealsByUserId(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    Pageable pageable = PageRequest.of(page, size);
    return service.getAllByUserId(pageable);
  }

  @GetMapping("/short")
  private Mono<Page<MealShortView>> getAllMealsByUserIdShortFoodResponse(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    Pageable pageable = PageRequest.of(page, size);
    return service.getAllByUserIdShorten(pageable);
  }

  @GetMapping("/{mealId}")
  private Mono<MealView> getMealById(@PathVariable String mealId) {
    return service.getByIdAndUserId(mealId);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  private Mono<MealView> createMeal(@RequestBody CreateMeal dto) {
    return service.createMeal(dto);
  }

  @PatchMapping("/{mealId}")
  private Mono<MealView> changeMeal(@RequestBody CreateMeal dto, @PathVariable String mealId) {
    return service.modifyMeal(dto, mealId);
  }

  @DeleteMapping("/{mealId}")
  private Mono<Void> deleteMealById(@PathVariable String mealId) {
    return service.deleteByIdAndUserId(mealId);
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ExceptionResponse> catchUserNotFound(BadRequestException e) {
    return Mono.just(new ExceptionResponse(e.getMessage()));
  }
}
