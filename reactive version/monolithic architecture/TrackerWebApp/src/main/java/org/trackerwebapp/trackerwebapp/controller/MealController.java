package org.trackerwebapp.trackerwebapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.trackerwebapp.trackerwebapp.config.security.UserPrincipal;
import org.trackerwebapp.trackerwebapp.domain.dto.BadRequestException;
import org.trackerwebapp.trackerwebapp.domain.dto.ExceptionResponse;
import org.trackerwebapp.trackerwebapp.domain.dto.meal.CreateMeal;
import org.trackerwebapp.trackerwebapp.domain.dto.meal.MealView;
import org.trackerwebapp.trackerwebapp.service.MealService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meals")
public class MealController {

  private final MealService service;

  @GetMapping
  private Flux<MealView> getAllByRecordId(@AuthenticationPrincipal UserPrincipal user) {
    return service.getAllByUserId(user.getId());
  }

  @GetMapping("/{mealId}")
  private Mono<MealView> getMealByIdAndRecordId(@AuthenticationPrincipal UserPrincipal user, @PathVariable String mealId) {
    return service.getByIdAndUserId(mealId, user.getId());
  }

  @PostMapping
  private Mono<MealView> createMeal(@AuthenticationPrincipal UserPrincipal user, @RequestBody CreateMeal dto) {
    return service.createMeal(user.getId(), dto);
  }

  @PatchMapping("/{mealId}")
  private Mono<MealView> changeMeal(@AuthenticationPrincipal UserPrincipal user, @RequestBody CreateMeal dto, @PathVariable String mealId) {
    return service.modifyMeal(user.getId(), dto, mealId);
  }

  @DeleteMapping("/{mealId}")
  private Mono<Void> deleteMealByIdAndRecordId(@AuthenticationPrincipal UserPrincipal user, @PathVariable String mealId) {
    return service.deleteByIdAndUserId(mealId, user.getId());
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ExceptionResponse> catchUserNotFound(BadRequestException e) {
    return Mono.just(new ExceptionResponse(e.getMessage()));
  }
}
