package org.trackerwebapp.meal_server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.trackerwebapp.meal_server.service.MealService;
import org.trackerwebapp.meal_server.domain.dto.CreateMeal;
import org.trackerwebapp.meal_server.domain.dto.InsertFoodDto;
import org.trackerwebapp.meal_server.domain.dto.MealView;
import org.trackerwebapp.shared_interfaces.domain.dto.ExceptionResponse;
import org.trackerwebapp.shared_interfaces.domain.exception.BadRequestException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meals")
public class MealController {

  private final MealService service;

  @GetMapping
  private Flux<MealView> getAllByRecordId(@AuthenticationPrincipal User user) {
    return service.getAllByUserId(user.getUsername());
  }

  @GetMapping("/{mealId}")
  private Mono<MealView> getMealByIdAndRecordId(
      @AuthenticationPrincipal User user,
      @PathVariable String mealId) {
    return service.getByIdAndUserId(mealId, user.getUsername());
  }

  @PostMapping
  private Mono<MealView> createMeal(
      @AuthenticationPrincipal User user,
      @RequestBody CreateMeal dto) {
    return service.createMeal(user.getUsername(), dto);
  }

  @PatchMapping("/{mealId}")
  private Mono<MealView> changeMeal(
      @AuthenticationPrincipal User user,
      @RequestBody CreateMeal dto,
      @PathVariable String mealId) {
    return service.modifyMeal(user.getUsername(), dto, mealId);
  }

  @DeleteMapping("/{mealId}")
  private Mono<Void> deleteMealByIdAndRecordId(
      @AuthenticationPrincipal User user,
      @PathVariable String mealId) {
    return service.deleteByIdAndUserId(mealId, user.getUsername());
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ExceptionResponse> catchUserNotFound(BadRequestException e) {
    return Mono.just(new ExceptionResponse(e.getMessage()));
  }
}
