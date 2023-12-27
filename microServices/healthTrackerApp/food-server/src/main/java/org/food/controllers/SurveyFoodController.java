package org.food.controllers;

import org.food.domain.dtos.FilterDataInfo;
import org.food.domain.dtos.foodView.*;
import org.food.exception.FoodException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(path = "/api/food/embedded/surveyFoods")
@RestController
public interface SurveyFoodController {
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
    SurveyFoodView getFinalFoodById(@PathVariable String id) throws FoodException;

    @PatchMapping("/calculate")
    public SurveyFoodView calculateFood(@RequestBody SurveyFoodView food,
                                  @RequestParam(name = "amount") Double amount) throws FoodException;
}
