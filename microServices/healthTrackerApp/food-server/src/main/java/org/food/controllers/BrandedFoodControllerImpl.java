package org.food.controllers;

import lombok.RequiredArgsConstructor;
import org.food.domain.dtos.FilterDataInfo;
import org.food.domain.dtos.foodView.BrandedFoodView;
import org.food.domain.dtos.foodView.FilteredFoodView;
import org.food.domain.dtos.foodView.NotCompleteFoodView;
import org.food.exception.FoodException;
import org.food.services.BrandedEmbeddedFoodServiceImp;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BrandedFoodControllerImpl implements BrandedFoodController {

    private final BrandedEmbeddedFoodServiceImp brandedFoodServiceImp;

    @Override
    public List<NotCompleteFoodView> getAllEmbeddedFoods() {
        return brandedFoodServiceImp.getAllEmbeddedFoods();
    }

    @Override
    public List<NotCompleteFoodView> getAllEmbeddedFoodSearchDescription(String description) throws FoodException {
        return brandedFoodServiceImp.getAllEmbeddedFoodsSearchDescription(description);
    }

    @Override
    public List<FilteredFoodView> getAllEmbeddedFoodFilter(String nutrientName, Boolean desc, Integer limit, Double min, Double max) throws FoodException {
        return brandedFoodServiceImp.getAllEmbeddedFoodFilter(new FilterDataInfo(nutrientName, desc, limit, max, min));
    }

    @Override
    public BrandedFoodView getFinalFoodById(String id) throws FoodException {
        return brandedFoodServiceImp.getFinalFoodById(id);
    }

    @Override
    public BrandedFoodView calculateFood(BrandedFoodView food, Double amount) throws FoodException {
        return brandedFoodServiceImp.calculateFood(food, amount);

    }
}
