package org.trackerwebapp.trackerwebapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.trackerwebapp.trackerwebapp.domain.dto.BadRequestException;
import org.trackerwebapp.trackerwebapp.domain.dto.ExceptionResponse;
import org.trackerwebapp.trackerwebapp.domain.dto.meal.InsertFoodDto;
import org.trackerwebapp.trackerwebapp.service.NutritionixApiService;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/food_db_api/search")
public class NutritionixApiController {

  private final NutritionixApiService service;

  @GetMapping
  public Mono<InsertFoodDto> getFoodBySearchCriteria(@RequestParam String term) {
    return service.getFoodBySearchTerm(term);
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
