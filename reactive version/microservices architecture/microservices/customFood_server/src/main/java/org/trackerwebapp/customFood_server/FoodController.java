package org.trackerwebapp.customFood_server;

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

import org.trackerwebapp.customFood_server.domain.dto.FoodView;
import org.trackerwebapp.customFood_server.domain.dto.InsertFoodDto;
import org.trackerwebapp.shared_interfaces.domain.dto.ExceptionResponse;
import org.trackerwebapp.shared_interfaces.domain.exception.BadRequestException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/custom/food")
public class FoodController {

  private final FoodService service;

  @GetMapping
  private Flux<FoodView> getAll(
      @AuthenticationPrincipal User user){
    return service.getAllFoods(user.getUsername());
  }

  @GetMapping("/{foodId}")
  private Mono<FoodView> getAll(
      @AuthenticationPrincipal User user,
      @PathVariable String foodId){
    return service.getById(user.getUsername() ,foodId);
  }

  @PostMapping
  private Mono<Void> addFood(
      @AuthenticationPrincipal User user,
      @RequestBody InsertFoodDto dto) {
    return service.createFood(user.getUsername(), dto);
  }

  @DeleteMapping("/{foodId}")
  private Mono<Void> deleteFoodFromMeal(
      @AuthenticationPrincipal User user,
      @PathVariable String foodId) {
    return service.deleteFood(user.getUsername(),foodId);
  }

  @PatchMapping("/{foodId}")
  private Mono<FoodView> changeFood(
      @AuthenticationPrincipal User user,
      @PathVariable String foodId,
      @RequestBody InsertFoodDto dto) {
    return service.changeFood(user.getUsername(), foodId , dto);
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ExceptionResponse> catchUserNotFound(BadRequestException e) {
    return Mono.just(new ExceptionResponse(e.getMessage()));
  }
}
