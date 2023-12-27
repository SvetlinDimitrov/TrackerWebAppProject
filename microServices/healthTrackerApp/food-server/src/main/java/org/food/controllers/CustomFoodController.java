package org.food.controllers;

import jakarta.validation.Valid;
import org.food.domain.dtos.CreateCustomFood;
import org.food.domain.dtos.foodView.CustomFoodView;
import org.food.domain.dtos.foodView.FoodView;
import org.food.domain.dtos.foodView.NotCompleteFoodView;
import org.food.exception.FoodException;
import org.food.exception.InvalidUserTokenHeaderException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(path = "/api/food")
@RestController
public interface CustomFoodController {


    @GetMapping(path = "/all")
    List<NotCompleteFoodView> getAllCustomFoods(@RequestHeader(name = "X-ViewUser") String userToken) throws InvalidUserTokenHeaderException;

    @GetMapping(path = "/search")
    List<NotCompleteFoodView> getAllEmbeddedFoodSearchDescription(@RequestParam String description , @RequestHeader(name = "X-ViewUser") String userToken) throws FoodException, InvalidUserTokenHeaderException;

    @GetMapping(path = "{id}")
    CustomFoodView getCustomFood(
            @RequestHeader(name = "X-ViewUser") String userToken,
            @PathVariable String id)
            throws FoodException, InvalidUserTokenHeaderException;

    @PostMapping
    ResponseEntity<Void> addFood(
            @Valid @RequestBody CreateCustomFood createCustomFood,
            BindingResult bindingResult,
            @RequestHeader(name = "X-ViewUser") String userToken)
            throws FoodException, InvalidUserTokenHeaderException;

    @DeleteMapping(path = "{id}")
    void deleteCustomFood(
            @RequestHeader(name = "X-ViewUser") String userToken,
            @PathVariable(name = "id") String id)
            throws FoodException, InvalidUserTokenHeaderException;

    @PatchMapping("/calculate")
    public CustomFoodView calculateFood (@RequestBody CustomFoodView food,
                                  @RequestHeader(name = "X-ViewUser") String userToken,
                                  @RequestParam(name = "amount") Double amount) throws FoodException;

}
