package org.trackerwebapp.trackerwebapp.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.trackerwebapp.trackerwebapp.config.security.UserPrincipal;
import org.trackerwebapp.trackerwebapp.domain.dto.BadRequestException;
import org.trackerwebapp.trackerwebapp.domain.dto.ExceptionResponse;
import org.trackerwebapp.trackerwebapp.domain.dto.custom_food.CustomFoodView;
import org.trackerwebapp.trackerwebapp.domain.dto.custom_food.CustomInsertFoodDto;
import org.trackerwebapp.trackerwebapp.service.CustomFoodService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/custom/food")
public class CustomFoodController {

  private final CustomFoodService service;

  @GetMapping
  private Flux<CustomFoodView> getAll(@AuthenticationPrincipal UserPrincipal user) {
    return service.getAllFoods(user.getId());
  }

  @GetMapping("/{foodId}")
  private Mono<CustomFoodView> getAll(@AuthenticationPrincipal UserPrincipal user, @PathVariable String foodId) {
    return service.getById(user.getId(), foodId);
  }

  @PostMapping
  private Mono<Void> addFood(@AuthenticationPrincipal UserPrincipal user, @RequestBody CustomInsertFoodDto dto) {
    return service.createFood(user.getId(), dto);
  }

  @DeleteMapping("/{foodId}")
  private Mono<Void> deleteFoodFromMeal(@AuthenticationPrincipal UserPrincipal user, @PathVariable String foodId) {
    return service.deleteFood(user.getId(), foodId);
  }

  @PatchMapping("/{foodId}")
  private Mono<CustomFoodView> changeFood(@AuthenticationPrincipal UserPrincipal user, @PathVariable String foodId, @RequestBody CustomInsertFoodDto dto) {
    return service.changeFood(user.getId(), foodId, dto);
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ExceptionResponse> catchUserNotFound(BadRequestException e) {
    return Mono.just(new ExceptionResponse(e.getMessage()));
  }
}

