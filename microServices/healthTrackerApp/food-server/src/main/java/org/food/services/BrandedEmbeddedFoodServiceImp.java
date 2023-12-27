package org.food.services;

import lombok.RequiredArgsConstructor;
import org.food.domain.dtos.FilterDataInfo;
import org.food.domain.dtos.foodView.BrandedFoodView;
import org.food.domain.dtos.foodView.FilteredFoodView;
import org.food.domain.dtos.foodView.FoodView;
import org.food.domain.dtos.foodView.NotCompleteFoodView;
import org.food.exception.FoodException;
import org.food.repositories.EmbeddedFoodRepository;
import org.food.utils.FoodUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandedEmbeddedFoodServiceImp extends DefaultEmbeddedFoodService {

    private final EmbeddedFoodRepository brandedFoodRepository;
    private final FoodUtils foodUtils;

    @Override
    public List<NotCompleteFoodView> getAllEmbeddedFoods() {
        return brandedFoodRepository.findAllProjectedByDescriptionAndName("brandedFoods");
    }

    @Override
    protected List<NotCompleteFoodView> getAllEmbeddedFoodsSearchDescriptionImp(String regex){
        return brandedFoodRepository.findAllProjectedByRegex(regex , "brandedFoods");
    }

    @Override
    protected List<FilteredFoodView> getAllEmbeddedFoodsByFilterImp(FilterDataInfo dataInfo , String type) throws FoodException {
        return brandedFoodRepository.executeAggregation(type , dataInfo , "brandedFoods");
    }

    @Override
    public BrandedFoodView getFinalFoodById(String id) throws FoodException {
        return brandedFoodRepository.findById(id , BrandedFoodView.class , "brandedFoods")
                .map(food -> foodUtils.toFoodView(food, BrandedFoodView.class))
                .orElseThrow(() -> new FoodException("Food with id " + id + " does not exist in brandedFoods type."));
    }
}
