package org.nutriGuideBuddy.controller;

import lombok.RequiredArgsConstructor;
import org.nutriGuideBuddy.domain.dto.BadRequestException;
import org.nutriGuideBuddy.domain.dto.ExceptionResponse;
import org.nutriGuideBuddy.domain.dto.meal.FoodView;
import org.nutriGuideBuddy.domain.dto.meal.InsertFoodDto;
import org.nutriGuideBuddy.domain.dto.meal.ShortenFood;
import org.nutriGuideBuddy.service.CustomFoodServiceImp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/custom/food")
public class CustomFoodController {

  private final CustomFoodServiceImp service;

  @GetMapping
  private Mono<Page<FoodView>> getAllCustomFoods(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    if (page < 0 || size < 0) {
      return Mono.error(new BadRequestException("Page and size must be greater than 0"));
    }

    Pageable pageable = PageRequest.of(page, size);
    return service.getAllFoods(pageable);
  }

  @GetMapping("/short")
  private Mono<Page<ShortenFood>> searchCustomFoods(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    if (page < 0 || size < 0) {
      return Mono.error(new BadRequestException("Page and size must be greater than 0"));
    }

    Pageable pageable = PageRequest.of(page, size);
    return service.getAllFoodsShort(pageable);
  }

  @GetMapping("/{foodId}")
  private Mono<FoodView> getCustomFoodById(@PathVariable String foodId) {
    return service.getById(foodId);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  private Mono<Void> addFood(@RequestBody InsertFoodDto dto) {
    return service.createFood(dto);
  }

  @DeleteMapping("/{foodId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  private Mono<Void> deleteFoodFromMeal(@PathVariable String foodId) {
    return service.deleteFood(foodId);
  }

  @PutMapping("/{foodId}")
  private Mono<FoodView> changeFood(@PathVariable String foodId, @RequestBody InsertFoodDto dto) {
    return service.changeFood(foodId, dto);
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ExceptionResponse> catchUserNotFound(BadRequestException e) {
    return Mono.just(new ExceptionResponse(e.getMessage()));
  }
}

