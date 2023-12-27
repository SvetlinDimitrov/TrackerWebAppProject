package org.food.controllers;

import lombok.RequiredArgsConstructor;
import org.food.domain.dtos.FilterDataInfo;
import org.food.domain.dtos.foodView.FilteredFoodView;
import org.food.domain.dtos.foodView.FoodView;
import org.food.domain.dtos.foodView.NotCompleteFoodView;
import org.food.domain.dtos.foodView.FinalFoodView;
import org.food.exception.FoodException;
import org.food.services.FinalEmbeddedFoodServiceImpl;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FinalFoodControllerImpl implements FinalFoodController {

    private final FinalEmbeddedFoodServiceImpl finalFoodService;

    @Override
    public List<NotCompleteFoodView> getAllEmbeddedFoods() {
        return finalFoodService.getAllEmbeddedFoods();
    }

    @Override
    public List<NotCompleteFoodView> getAllEmbeddedFoodSearchDescription(String description) throws FoodException {
        return finalFoodService.getAllEmbeddedFoodsSearchDescription(description);
    }

    @Override
    public List<FilteredFoodView> getAllEmbeddedFoodFilter(String nutrientName, Boolean desc, Integer limit, Double min, Double max) throws FoodException {
        return finalFoodService.getAllEmbeddedFoodFilter(new FilterDataInfo(nutrientName, desc, limit, max, min));
    }
    @Override
    public FinalFoodView getFinalFoodById(String id) throws FoodException {
        return finalFoodService.getFinalFoodById(id);
    }

    @Override
    public FinalFoodView calculateFood(FinalFoodView food, Double amount) throws FoodException {
        return finalFoodService.calculateFood(food, amount);
    }
}
