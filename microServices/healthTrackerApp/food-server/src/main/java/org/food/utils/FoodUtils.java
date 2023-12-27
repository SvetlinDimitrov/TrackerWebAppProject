package org.food.utils;

import lombok.RequiredArgsConstructor;
import org.food.clients.dto.User;
import org.food.domain.dtos.FilterDataInfo;
import org.food.domain.dtos.foodView.storageFoodView.CaloriesView;
import org.food.domain.dtos.foodView.FoodView;
import org.food.domain.dtos.foodView.storageFoodView.NutrientView;
import org.food.domain.entity.CustomFoodEntity;
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

        BigDecimal multiplayer = amount.divide(food.getSize(), 2, RoundingMode.HALF_UP);

        food.setCalories(new CaloriesView(food.getCalories().getAmount().multiply(multiplayer)));

        List<NutrientView> vitaminNutrients = new ArrayList<>();
        food.getMineralNutrients().forEach(nutrient -> {
            NutrientView nutrientView = new NutrientView();
            nutrientView.setName(nutrient.getName());
            nutrientView.setUnit(nutrient.getUnit());
            nutrientView.setAmount(nutrient.getAmount().multiply(multiplayer));
            vitaminNutrients.add(nutrientView);
        });
        food.setVitaminNutrients(vitaminNutrients);

        List<NutrientView> mineralNutrients = new ArrayList<>();
        food.getMineralNutrients().forEach(nutrient -> {
            NutrientView nutrientView = new NutrientView();
            nutrientView.setName(nutrient.getName());
            nutrientView.setUnit(nutrient.getUnit());
            nutrientView.setAmount(nutrient.getAmount().multiply(multiplayer));
            mineralNutrients.add(nutrientView);
        });
        food.setMineralNutrients(mineralNutrients);

        List<NutrientView> macronutrients = new ArrayList<>();
        food.getMacronutrients().forEach(nutrient -> {
            NutrientView nutrientView = new NutrientView();
            nutrientView.setName(nutrient.getName());
            nutrientView.setUnit(nutrient.getUnit());
            nutrientView.setAmount(nutrient.getAmount().multiply(multiplayer));
            macronutrients.add(nutrientView);
        });
        food.setMacronutrients(macronutrients);
    }

    public <F,T> F toFoodView(T food, Class<F> foodViewClass) {
        return modelMapper.map(food, foodViewClass);
    }
    public CustomFoodEntity toCustomFoodEntity(FoodView entity) {
        return modelMapper.map(entity, CustomFoodEntity.class);
    }

    public Long getUserId(String userToken) throws InvalidUserTokenHeaderException {
        try {
            return gsonWrapper.fromJson(userToken, User.class).getId();
        } catch (Exception e) {
            throw new InvalidUserTokenHeaderException("Invalid user token header.");
        }
    }

}
