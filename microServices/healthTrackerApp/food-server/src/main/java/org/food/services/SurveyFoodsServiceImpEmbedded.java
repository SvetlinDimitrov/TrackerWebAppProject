package org.food.services;

import lombok.RequiredArgsConstructor;
import org.food.domain.dtos.FilterDataInfo;
import org.food.domain.dtos.foodView.FilteredFoodView;
import org.food.domain.dtos.foodView.NotCompleteFoodView;
import org.food.domain.dtos.foodView.SurveyFoodView;
import org.food.exception.FoodException;
import org.food.repositories.EmbeddedFoodRepository;
import org.food.utils.FoodUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyFoodsServiceImpEmbedded extends DefaultEmbeddedFoodService {

    private final EmbeddedFoodRepository surveyFoodsRepository;
    private final FoodUtils foodUtils;

    @Override
    public List<NotCompleteFoodView> getAllEmbeddedFoods() {
        return surveyFoodsRepository.findAllProjectedByDescriptionAndName("surveyFoods");
    }

    @Override
    protected List<NotCompleteFoodView> getAllEmbeddedFoodsSearchDescriptionImp(String description) {
        return surveyFoodsRepository.findAllProjectedByRegex(description, "surveyFoods");
    }

    @Override
    protected List<FilteredFoodView> getAllEmbeddedFoodsByFilterImp(FilterDataInfo dataInfo, String type) {
        return surveyFoodsRepository.executeAggregation(type, dataInfo, "surveyFoods");
    }

    @Override
    public SurveyFoodView getFinalFoodById(String id) throws FoodException {
        return surveyFoodsRepository.findById(id, SurveyFoodView.class, "surveyFoods")
                .map(food -> foodUtils.toFoodView(food, SurveyFoodView.class))
                .orElseThrow(() -> new FoodException("Food not found with id: " + id + " in surveyFood category !"));
    }
}
