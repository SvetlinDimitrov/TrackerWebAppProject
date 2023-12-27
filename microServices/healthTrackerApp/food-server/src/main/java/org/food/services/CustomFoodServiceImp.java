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
        Long userId = foodUtils.getUserId(userToken);
        return customFoodRepository.findAllProjectedByDescriptionAndNameByUserId(userId);
    }

    public List<NotCompleteFoodView> getAllEmbeddedFoodSearchDescription(String regex , String user) throws FoodException, InvalidUserTokenHeaderException {
        if(regex == null || regex.isBlank() || regex.length() < 3) {
            throw new FoodException("Search description must be at least 3 characters long.");
        }
        Long userId = foodUtils.getUserId(user);

        return customFoodRepository.findAllProjectedByRegex(regex , userId);
    }

    public CustomFoodView getCustomFoodByNameAndUserId(String id, String userToken) throws FoodException, InvalidUserTokenHeaderException {
        Long userId = foodUtils.getUserId(userToken);

        CustomFoodEntity food = customFoodRepository
                .findByIdAndUserId(id, userId)
                .orElseThrow(() -> new FoodException("Food with id " + id + " does not exist."));

        return foodUtils.toFoodView(food , CustomFoodView.class);
    }

    public void addCustomFood(CreateCustomFood customFood, String userToken) throws FoodException, InvalidUserTokenHeaderException {
        Long userId = foodUtils.getUserId(userToken);
        CustomFoodEntity food = toCustomFoodEntity(customFood);

        if (customFoodRepository.findByDescriptionAndUserId(customFood.getName(), userId).isPresent()) {
            throw new FoodException("Food with name " + customFood.getName() + " already exists.");
        }

        food.setUserId(userId);
        customFoodRepository.save(food);
    }

    public void deleteCustomFood(String id, String userToken) throws FoodException, InvalidUserTokenHeaderException {
        Long userId = foodUtils.getUserId(userToken);

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

        foodEntity.setDescription(entity.getName());
        foodEntity.setFoodClass("Custom");
        foodEntity.setSize(entity.getSize());
        foodEntity.setMeasurement("grams");
        foodEntity.setCalories(new Calories(entity.getCalories()));
        foodEntity.setMineralNutrients(toNutrientList(entity, "Minerals"));
        foodEntity.setVitaminNutrients(toNutrientList(entity, "Vitamins"));
        foodEntity.setMacronutrients(toNutrientList(entity, "Macronutrients"));

        foodUtils.calculateFoodByAmount(foodUtils.toFoodView(foodEntity , FoodView.class), 100.0);

        return foodEntity;
    }

    private List<Nutrient> toNutrientList(CreateCustomFood entity, String nutrientType) {

        List<Nutrient> nutrientViewList = new ArrayList<>();

        switch (nutrientType) {
            case "Vitamins" -> {
                nutrientViewList.add(new Nutrient("Vitamin A", "µg", entity.getA()));
                nutrientViewList.add(new Nutrient("Vitamin D (D2 + D3)", "µg", entity.getD()));
                nutrientViewList.add(new Nutrient("Vitamin E", "mg", entity.getE()));
                nutrientViewList.add(new Nutrient("Vitamin K", "µg", entity.getK()));
                nutrientViewList.add(new Nutrient("Vitamin C", "mg", entity.getC()));
                nutrientViewList.add(new Nutrient("Vitamin B1 (Thiamin)", "mg", entity.getB1()));
                nutrientViewList.add(new Nutrient("Vitamin B2 (Riboflavin)", "mg", entity.getB2()));
                nutrientViewList.add(new Nutrient("Vitamin B3 (Niacin)", "mg", entity.getB3()));
                nutrientViewList.add(new Nutrient("Vitamin B5 (Pantothenic Acid)", "mg", entity.getB5()));
                nutrientViewList.add(new Nutrient("Vitamin B6", "mg", entity.getB6()));
                nutrientViewList.add(new Nutrient("Vitamin B7 (Biotin)", "µg", entity.getB7()));
                nutrientViewList.add(new Nutrient("Vitamin B9 (Folate)", "µg", entity.getB9()));
                nutrientViewList.add(new Nutrient("Vitamin B12", "µg", entity.getB12()));
            }
            case "Minerals" -> {
                nutrientViewList.add(new Nutrient("Calcium , Ca", "mg", entity.getCalcium()));
                nutrientViewList.add(new Nutrient("Phosphorus , P", "mg", entity.getPhosphorus()));
                nutrientViewList.add(new Nutrient("Magnesium , Mg", "mg", entity.getMagnesium()));
                nutrientViewList.add(new Nutrient("Sodium , Na", "mg", entity.getSodium()));
                nutrientViewList.add(new Nutrient("Potassium , K", "mg", entity.getPotassium()));
                nutrientViewList.add(new Nutrient("Iron , Fe", "mg", entity.getIron()));
                nutrientViewList.add(new Nutrient("Zinc , Zn", "mg", entity.getZinc()));
                nutrientViewList.add(new Nutrient("Copper , Cu", "mg", entity.getCopper()));
                nutrientViewList.add(new Nutrient("Manganese , Mn", "mg", entity.getManganese()));
                nutrientViewList.add(new Nutrient("Selenium , Se", "µg", entity.getSelenium()));
                nutrientViewList.add(new Nutrient("Iodine , I", "µg", entity.getIodine()));
                nutrientViewList.add(new Nutrient("Molybdenum , Mo", "µg", entity.getMolybdenum()));
            }
            case "Macronutrients" -> {
                nutrientViewList.add(new Nutrient("Carbohydrates", "g", entity.getCarbohydrates()));
                nutrientViewList.add(new Nutrient("Protein", "g", entity.getProtein()));
                nutrientViewList.add(new Nutrient("Fat", "g", entity.getFat()));
                nutrientViewList.add(new Nutrient("Fiber", "g", entity.getFiber()));
                nutrientViewList.add(new Nutrient("Trans Fat", "g", entity.getTransFat()));
                nutrientViewList.add(new Nutrient("Saturated Fat", "g", entity.getSaturatedFat()));
                nutrientViewList.add(new Nutrient("Monounsaturated Fat", "g", entity.getMonounsaturatedFat()));
                nutrientViewList.add(new Nutrient("Polyunsaturated Fat", "g", entity.getPolyunsaturatedFat()));
                nutrientViewList.add(new Nutrient("Sugar", "g", entity.getSugar()));
            }
        }

        return nutrientViewList;
    }
}
