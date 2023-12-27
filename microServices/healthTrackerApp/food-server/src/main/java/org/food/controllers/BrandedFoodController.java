package org.food.controllers;

import org.food.domain.dtos.FilterDataInfo;
import org.food.domain.dtos.foodView.BrandedFoodView;
import org.food.domain.dtos.foodView.FilteredFoodView;
import org.food.domain.dtos.foodView.FoodView;
import org.food.domain.dtos.foodView.NotCompleteFoodView;
import org.food.exception.FoodException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(path = "/api/food/embedded/brandedFoods")
@RestController
public interface BrandedFoodController {
    @GetMapping(path = "/all")
    List<NotCompleteFoodView> getAllEmbeddedFoods();

    @GetMapping(path = "/search")
    List<NotCompleteFoodView> getAllEmbeddedFoodSearchDescription(@RequestParam  String description) throws FoodException;

    @GetMapping(path = "/filter")
    List<FilteredFoodView> getAllEmbeddedFoodFilter(@RequestParam String nutrientName,
                                                    @RequestParam(defaultValue = "true") Boolean desc,
                                                    @RequestParam(defaultValue = "50") Integer limit ,
                                                    @RequestParam(defaultValue = "0.0") Double min ,
                                                    @RequestParam(defaultValue = "100.0") Double max) throws FoodException;

    @GetMapping(path = "/{id}")
    BrandedFoodView getFinalFoodById(@PathVariable String id) throws FoodException;

    @PatchMapping("/calculate")
    public FoodView calculateFood(@RequestBody BrandedFoodView food,
                                  @RequestParam(name = "amount") Double amount) throws FoodException;
}

