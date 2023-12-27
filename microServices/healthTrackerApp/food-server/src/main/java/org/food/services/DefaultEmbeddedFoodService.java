package org.food.services;

import org.food.domain.dtos.FilterDataInfo;
import org.food.domain.dtos.foodView.FilteredFoodView;
import org.food.domain.dtos.foodView.FoodView;
import org.food.domain.dtos.foodView.NotCompleteFoodView;
import org.food.exception.FoodException;
import org.food.utils.FoodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
public abstract class DefaultEmbeddedFoodService {

    @Autowired
    public FoodUtils foodUtils;

    abstract List<NotCompleteFoodView> getAllEmbeddedFoods();
    public List<NotCompleteFoodView> getAllEmbeddedFoodsSearchDescription(String regex) throws FoodException {
        if(regex == null || regex.isBlank() || regex.length() < 3) {
            throw new FoodException("Search description must be at least 3 characters long.");
        }
        return getAllEmbeddedFoodsSearchDescriptionImp(regex);
    }
    protected abstract List<NotCompleteFoodView> getAllEmbeddedFoodsSearchDescriptionImp(String description);

    public List<FilteredFoodView> getAllEmbeddedFoodFilter(FilterDataInfo dataInfo) throws FoodException {
        String type = validateFilterDataInfo(dataInfo);
        return getAllEmbeddedFoodsByFilterImp(dataInfo , type);
    }
    protected abstract List<FilteredFoodView> getAllEmbeddedFoodsByFilterImp (FilterDataInfo dataInfo , String type) throws FoodException;

    public abstract FoodView getFinalFoodById(String id) throws FoodException;

    public <T extends FoodView> T calculateFood(T food, Double amount) throws FoodException {
        validateAmount(amount);
        foodUtils.calculateFoodByAmount(food, amount);
        return food;
    }

    private void validateAmount(Double amount) throws FoodException {
        if(amount == null || amount <= 0) {
            throw new FoodException("Amount must be greater than 0.");
        }
    }

    private String validateFilterDataInfo(FilterDataInfo filterDataInfo) throws FoodException {

        if(filterDataInfo.getDesc() == null) {
            throw new FoodException("Sorting method cannot be empty.");
        }
        if(filterDataInfo.getMax() == null ||
                filterDataInfo.getMin() == null ||
                filterDataInfo.getMin().compareTo(0.0) < 0 ||
                filterDataInfo.getMax().compareTo(0.0) < 0 ||
                filterDataInfo.getMax().compareTo(filterDataInfo.getMin()) < 0) {
            throw new FoodException("Invalid min or max value.");
        }
        if(filterDataInfo.getNutrientName() == null || filterDataInfo.getNutrientName().isEmpty()) {
            throw new FoodException("Nutrient name cannot be empty.");
        }
        String type = validateNutrientName(filterDataInfo.getNutrientName());
        if(filterDataInfo.getLimit() == null || filterDataInfo.getLimit() < 0) {
            throw new FoodException("Invalid limit value.");
        }
        return type;
    }
    private String validateNutrientName(String name) throws FoodException {
        Set<String> vitaminNames = Set.of(
                "Vitamin A",
                "Vitamin D (D2 + D3)",
                "Vitamin E",
                "Vitamin K",
                "Vitamin C",
                "Vitamin B1 (Thiamin)",
                "Vitamin B2 (Riboflavin)",
                "Vitamin B3 (Niacin)",
                "Vitamin B5 (Pantothenic acid)",
                "Vitamin B6",
                "Vitamin B7 (Biotin)",
                "Vitamin B9 (Folate)",
                "Vitamin B12"
        );
        Set<String> macronutrientNames = Set.of(
                "Carbohydrates",
                "Protein",
                "Fat",
                "Fiber",
                "Trans Fat",
                "Saturated Fat",
                "Sugar",
                "Polyunsaturated Fat",
                "Monounsaturated Fat"
        );


        Set<String> mineralNames = Set.of(
                "Calcium , Ca",
                "Phosphorus , P",
                "Magnesium , Mg",
                "Sodium , Na",
                "Potassium , K",
                "Iron , Fe",
                "Zinc , Zn",
                "Copper , Cu",
                "Manganese , Mn",
                "Iodine , I",
                "Selenium , Se",
                "Molybdenum , Mo"
        );

        if(name == null || name.isEmpty()) {
            throw new FoodException("Nutrient name cannot be empty.");
        }
        if(vitaminNames.contains(name)) {
            return "vitaminNutrients";
        }else if (macronutrientNames.contains(name)){
            return "macronutrients";
        }else if (mineralNames.contains(name)){
            return "mineralNutrients";
        }

        throw new FoodException("Invalid nutrient name.");
    }
}
