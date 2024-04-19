package org.food.utils;

import lombok.RequiredArgsConstructor;
import org.food.clients.dto.User;
import org.food.domain.dtos.CreateCustomFood;
import org.food.domain.dtos.foodView.FoodView;
import org.food.domain.dtos.foodView.storageFoodView.CaloriesView;
import org.food.domain.dtos.foodView.storageFoodView.NutrientView;
import org.food.domain.entity.storageEntity.Nutrient;
import org.food.exception.FoodException;
import org.food.exception.InvalidUserTokenHeaderException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class FoodUtils {

    private final ModelMapper modelMapper;
    private final GsonWrapper gsonWrapper;

    public <T extends FoodView> void calculateFoodByAmount(T food, Double size) {

        BigDecimal amount = BigDecimal.valueOf(size);

        if (food.getSize().compareTo(amount) == 0) {
            return;
        }

        BigDecimal multiplayer = amount.divide(food.getSize(), 20, RoundingMode.HALF_UP);

        food.setCalories(new CaloriesView(food.getCalories().getAmount().multiply(multiplayer).setScale(2, RoundingMode.HALF_UP)));
        food.setSize(amount);

        List<NutrientView> vitaminNutrients = new ArrayList<>();
        if (food.getVitaminNutrients() != null) {
            food.getVitaminNutrients().forEach(nutrient -> {
                NutrientView nutrientView = new NutrientView();
                nutrientView.setName(nutrient.getName());
                nutrientView.setUnit(nutrient.getUnit());
                nutrientView.setAmount(nutrient.getAmount().multiply(multiplayer).setScale(2, RoundingMode.HALF_UP));
                vitaminNutrients.add(nutrientView);
            });
        }
        food.setVitaminNutrients(vitaminNutrients);
        List<NutrientView> mineralNutrients = new ArrayList<>();
        if (food.getMineralNutrients() != null) {
            food.getMineralNutrients().forEach(nutrient -> {
                NutrientView nutrientView = new NutrientView();
                nutrientView.setName(nutrient.getName());
                nutrientView.setUnit(nutrient.getUnit());
                nutrientView.setAmount(nutrient.getAmount().multiply(multiplayer).setScale(2, RoundingMode.HALF_UP));
                mineralNutrients.add(nutrientView);
            });
        }
        food.setMineralNutrients(mineralNutrients);
        List<NutrientView> macronutrients = new ArrayList<>();
        if (food.getMacroNutrients() != null) {
            food.getMacroNutrients().forEach(nutrient -> {
                NutrientView nutrientView = new NutrientView();
                nutrientView.setName(nutrient.getName());
                nutrientView.setUnit(nutrient.getUnit());
                nutrientView.setAmount(nutrient.getAmount().multiply(multiplayer).setScale(2, RoundingMode.HALF_UP));
                macronutrients.add(nutrientView);
            });
        }
        food.setMacroNutrients(macronutrients);
    }

    public <F, T> F toFoodView(T food, Class<F> foodViewClass) {
        return modelMapper.map(food, foodViewClass);
    }

    public String getUserId(String userToken) throws InvalidUserTokenHeaderException {
        try {
            return gsonWrapper.fromJson(userToken, User.class).getId();
        } catch (Exception e) {
            throw new InvalidUserTokenHeaderException("Invalid user token header.");
        }
    }

    public void validateFood(CreateCustomFood food) throws FoodException {
        if (food == null) {
            throw new FoodException("Food is required.");
        }
        if (food.getDescription() == null || food.getDescription().isEmpty()) {
            throw new FoodException("Food name is required.");
        }
        if (food.getSize() == null || food.getSize().compareTo(BigDecimal.ZERO) < 0) {
            throw new FoodException("Invalid size.");
        }
        if (food.getCalories() == null || food.getCalories().compareTo(BigDecimal.ZERO) < 0) {
            throw new FoodException("Invalid calories.");
        }
        validateMinerals(food.getMineralNutrients());
        validateVitamins(food.getVitaminNutrients());
        validateMacronutrients(food.getMacroNutrients());
    }

    private void validateMacronutrients(List<Nutrient> macroNutrients) throws FoodException {
        Set<String> macronutrientNames = Set.of(
                "Carbohydrate",
                "Protein",
                "Fat",
                "Fiber",
                "Trans Fat",
                "Saturated Fat",
                "Sugar",
                "Polyunsaturated Fat",
                "Monounsaturated Fat"
        );
        if (macroNutrients == null || macroNutrients.isEmpty()) {
            return;
        }
        for (Nutrient nutrient : macroNutrients) {
            if (nutrient.getName() == null || nutrient.getName().isEmpty() ||
                    nutrient.getAmount() == null || nutrient.getAmount().compareTo(BigDecimal.ZERO) < 0 ||
                    nutrient.getUnit() == null || nutrient.getUnit().isEmpty()) {
                throw new FoodException("Invalid macronutrient.");
            }
            if (!macronutrientNames.contains(nutrient.getName())) {
                throw new FoodException("Invalid macronutrient. " + nutrient.getName() + " Valid names: " + String.join(", ", macronutrientNames));
            }
        }
    }

    private void validateVitamins(List<Nutrient> vitaminNutrients) throws FoodException {
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
        if (vitaminNutrients == null || vitaminNutrients.isEmpty()) {
            return;
        }
        for (Nutrient nutrient : vitaminNutrients) {
            if (nutrient.getName() == null || nutrient.getName().isEmpty() ||
                    nutrient.getAmount() == null || nutrient.getAmount().compareTo(BigDecimal.ZERO) < 0 ||
                    nutrient.getUnit() == null || nutrient.getUnit().isEmpty()) {
                throw new FoodException("Invalid vitamin nutrient.");
            }
            if (!vitaminNames.contains(nutrient.getName())) {
                throw new FoodException("Invalid vitamin nutrient. " + nutrient.getName() +
                        " Valid names: " + String.join(", ", vitaminNames));
            }
        }
    }

    private void validateMinerals(List<Nutrient> mineralNutrients) throws FoodException {
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
        if (mineralNutrients == null || mineralNutrients.isEmpty()) {
            return;
        }
        for (Nutrient nutrient : mineralNutrients) {
            if (nutrient.getName() == null || nutrient.getName().isEmpty() ||
                    nutrient.getAmount() == null || nutrient.getAmount().compareTo(BigDecimal.ZERO) < 0 ||
                    nutrient.getUnit() == null || nutrient.getUnit().isEmpty()) {
                throw new FoodException("Invalid mineral nutrient.");
            }
            if (!mineralNames.contains(nutrient.getName())) {
                throw new FoodException("Invalid mineral nutrient. " + nutrient.getName() +
                        " Valid names: " + String.join(", ", mineralNames));
            }
        }
    }
}
