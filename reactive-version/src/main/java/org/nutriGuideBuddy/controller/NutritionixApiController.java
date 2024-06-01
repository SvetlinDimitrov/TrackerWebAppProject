package org.nutriGuideBuddy.controller;

import lombok.RequiredArgsConstructor;
import org.nutriGuideBuddy.domain.dto.BadRequestException;
import org.nutriGuideBuddy.domain.dto.ExceptionResponse;
import org.nutriGuideBuddy.domain.dto.nutritionxApi.ListFoodsResponse;
import org.nutriGuideBuddy.service.NutritionixApiService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.nutriGuideBuddy.domain.dto.meal.InsertFoodDto;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/food_db_api/search")
public class NutritionixApiController {

  private final NutritionixApiService service;

  //Mostly return a single food
  @GetMapping("/common/{term}")
  public Mono<List<InsertFoodDto>> getFoodBySearchCriteria(@PathVariable String term) {
    return service.getCommonFoodBySearchTerm(term);
  }

  //Mostly return a single food
  @GetMapping("/branded/{id}")
  public Mono<List<InsertFoodDto>> getBrandedFoodById(@PathVariable String id) {
    return service.getBrandedFoodById(id);
  }

  @GetMapping
  public Mono<ListFoodsResponse> getFoodsByName(@RequestParam String foodName) {
    return service.getAllFoodsByFoodName(foodName);
  }

//  @GetMapping("/{foodId}")
//  public List<InsertFoodDto> getFoodById(@PathVariable String foodId){
//
//  }
@ExceptionHandler(BadRequestException.class)
@ResponseStatus(HttpStatus.BAD_REQUEST)
public Mono<ExceptionResponse> catchUserNotFound(BadRequestException e) {
  return Mono.just(new ExceptionResponse(e.getMessage()));
}
}
