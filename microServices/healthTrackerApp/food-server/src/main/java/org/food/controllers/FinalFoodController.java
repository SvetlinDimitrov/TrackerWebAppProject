package org.food.controllers;

import org.food.domain.dtos.FilterDataInfo;
import org.food.domain.dtos.foodView.FilteredFoodView;
import org.food.domain.dtos.foodView.FoodView;
import org.food.domain.dtos.foodView.NotCompleteFoodView;
import org.food.domain.dtos.foodView.FinalFoodView;
import org.food.exception.FoodException;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequestMapping(path = "/api/food/embedded/finalFoods")
@RestController
public interface FinalFoodController {
    @GetMapping(path = "/all")
    List<NotCompleteFoodView> getAllEmbeddedFoods();

    @GetMapping(path = "/search")
    List<NotCompleteFoodView> getAllEmbeddedFoodSearchDescription(@RequestParam String description) throws FoodException;

    @GetMapping(path = "/filter")
    List<FilteredFoodView> getAllEmbeddedFoodFilter(@RequestParam String nutrientName,
                                                    @RequestParam(defaultValue = "true") Boolean desc,
                                                    @RequestParam(defaultValue = "50") Integer limit ,
                                                    @RequestParam(defaultValue = "0.0") Double min ,
                                                    @RequestParam(defaultValue = "100.0") Double max) throws FoodException;

    @GetMapping(path = "/{id}")
    FinalFoodView getFinalFoodById(@PathVariable String id) throws FoodException;

    @PatchMapping("/calculate")
    public FinalFoodView calculateFood(@RequestBody FinalFoodView food,
                                       @RequestParam(name = "amount") Double amount) throws FoodException;
}
