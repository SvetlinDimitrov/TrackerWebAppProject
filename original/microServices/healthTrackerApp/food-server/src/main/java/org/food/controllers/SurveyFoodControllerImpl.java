package org.food.controllers;

import lombok.RequiredArgsConstructor;
import org.food.domain.dtos.FilterDataInfo;
import org.food.domain.dtos.foodView.FilteredFoodView;
import org.food.domain.dtos.foodView.NotCompleteFoodView;
import org.food.domain.dtos.foodView.SurveyFoodView;
import org.food.exception.FoodException;
import org.food.services.SurveyFoodsServiceImpEmbedded;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SurveyFoodControllerImpl implements SurveyFoodController {

    private final SurveyFoodsServiceImpEmbedded surveyFoodsServiceImp;

    @Override
    public List<NotCompleteFoodView> getAllEmbeddedFoods() {
        return surveyFoodsServiceImp.getAllEmbeddedFoods();
    }

    @Override
    public List<NotCompleteFoodView> getAllEmbeddedFoodSearchDescription(String description) throws FoodException {
        return surveyFoodsServiceImp.getAllEmbeddedFoodsSearchDescription(description);
    }

    @Override
    public List<FilteredFoodView> getAllEmbeddedFoodFilter(String nutrientName, Boolean desc, Integer limit, Double min, Double max) throws FoodException {
        return surveyFoodsServiceImp.getAllEmbeddedFoodFilter(new FilterDataInfo(nutrientName, desc, limit, max, min));
    }

    @Override
    public SurveyFoodView getFinalFoodById(String id) throws FoodException {
        return surveyFoodsServiceImp.getFinalFoodById(id);
    }

    @Override
    public SurveyFoodView calculateFood(SurveyFoodView food, Double amount) throws FoodException {
        return surveyFoodsServiceImp.calculateFood(food, amount);
    }
}
