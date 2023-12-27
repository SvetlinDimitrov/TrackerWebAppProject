package org.food.services;

import lombok.RequiredArgsConstructor;
import org.food.domain.dtos.FilterDataInfo;
import org.food.domain.dtos.foodView.FilteredFoodView;
import org.food.domain.dtos.foodView.FoodView;
import org.food.domain.dtos.foodView.NotCompleteFoodView;
import org.food.domain.dtos.foodView.FinalFoodView;
import org.food.exception.FoodException;
import org.food.repositories.EmbeddedFoodRepository;
import org.food.utils.FoodUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FinalEmbeddedFoodServiceImpl extends DefaultEmbeddedFoodService {

    private final EmbeddedFoodRepository embeddedFoodRepository;

    @Override
    public List<NotCompleteFoodView> getAllEmbeddedFoods() {
        return embeddedFoodRepository.findAllProjectedByDescriptionAndName("finalFoods");
    }

    @Override
    protected List<FilteredFoodView> getAllEmbeddedFoodsByFilterImp(FilterDataInfo dataInfo , String type){
        return embeddedFoodRepository.executeAggregation(type , dataInfo , "finalFoods");
    }

    @Override
    public FinalFoodView getFinalFoodById(String id) throws FoodException {
        return embeddedFoodRepository.findById(id, FinalFoodView.class, "finalFoods")
                .orElseThrow(() -> new FoodException("Food not found with id: " + id + " in finalFood category !"));
    }


    @Override
    public List<NotCompleteFoodView> getAllEmbeddedFoodsSearchDescriptionImp(String description){
        return embeddedFoodRepository.findAllProjectedByRegex(description , "finalFoods");
    }
}
