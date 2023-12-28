package org.food.services;

import lombok.RequiredArgsConstructor;
import org.food.domain.dtos.CreateCustomFood;
import org.food.domain.dtos.FilterDataInfo;
import org.food.domain.dtos.foodView.CustomFoodView;
import org.food.domain.dtos.foodView.FoodView;
import org.food.domain.dtos.foodView.NotCompleteFoodView;
import org.food.domain.entity.CustomFoodEntity;
import org.food.domain.entity.storageEntity.Calories;
import org.food.domain.entity.storageEntity.Nutrient;
import org.food.exception.FoodException;
import org.food.exception.InvalidUserTokenHeaderException;
import org.food.repositories.CustomFoodRepository;
import org.food.utils.FoodUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomFoodServiceImp{

    private final CustomFoodRepository customFoodRepository;
    private final FoodUtils foodUtils;

    public List<NotCompleteFoodView> getAllCustomFoods(String userToken) throws InvalidUserTokenHeaderException {
        String userId = foodUtils.getUserId(userToken);
        return customFoodRepository.findAllProjectedByDescriptionAndNameByUserId(userId);
    }

    public List<NotCompleteFoodView> getAllEmbeddedFoodSearchDescription(String regex , String user) throws FoodException, InvalidUserTokenHeaderException {
        if(regex == null || regex.isBlank() || regex.length() < 3) {
            throw new FoodException("Search description must be at least 3 characters long.");
        }
        String userId = foodUtils.getUserId(user);

        return customFoodRepository.findAllProjectedByRegex(regex , userId);
    }

    public CustomFoodView getCustomFoodByNameAndUserId(String id, String userToken) throws FoodException, InvalidUserTokenHeaderException {
        String userId = foodUtils.getUserId(userToken);

        CustomFoodEntity food = customFoodRepository
                .findByIdAndUserId(id, userId)
                .orElseThrow(() -> new FoodException("Food with id " + id + " does not exist."));

        return foodUtils.toFoodView(food , CustomFoodView.class);
    }

    public void addCustomFood(CreateCustomFood customFood, String userToken) throws FoodException, InvalidUserTokenHeaderException {
        String userId = foodUtils.getUserId(userToken);
        foodUtils.validateFood(customFood);

        CustomFoodEntity food = toCustomFoodEntity(customFood);


        food.setUserId(userId);
        customFoodRepository.save(food);
    }

    public void deleteCustomFood(String id, String userToken) throws FoodException, InvalidUserTokenHeaderException {
        String userId = foodUtils.getUserId(userToken);

        CustomFoodEntity food = customFoodRepository
                .findByIdAndUserId(id, userId)
                .orElseThrow(() -> new FoodException("Food with id " + id + " does not exist."));

        customFoodRepository.delete(food);
    }

    public CustomFoodView calculateNutrients(FoodView food, Double amount) throws FoodException {

        if (amount <= 0) {
            throw new FoodException("Amount must be greater than 0.");
        }
        foodUtils.calculateFoodByAmount(food, amount);

        return foodUtils.toFoodView(food, CustomFoodView.class);
    }

    private CustomFoodEntity toCustomFoodEntity(CreateCustomFood entity) {
        CustomFoodEntity foodEntity = new CustomFoodEntity();

        foodEntity.setDescription(entity.getDescription());
        foodEntity.setFoodClass("Custom");
        foodEntity.setSize(entity.getSize());
        foodEntity.setMeasurement("g");
        foodEntity.setCalories(new Calories(entity.getCalories()));
        foodEntity.setMineralNutrients(entity.getMineralNutrients());
        foodEntity.setVitaminNutrients(entity.getVitaminNutrients());
        foodEntity.setMacronutrients(entity.getMacronutrients());

        foodUtils.calculateFoodByAmount(foodUtils.toFoodView(foodEntity , FoodView.class), 100.0);

        return foodEntity;
    }
}
