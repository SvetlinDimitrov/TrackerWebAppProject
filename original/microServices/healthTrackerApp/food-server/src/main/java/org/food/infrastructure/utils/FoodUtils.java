package org.food.infrastructure.utils;

import static org.food.infrastructure.exception.ExceptionMessages.INVALID_USER_TOKEN;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.domain.user.UserView;
import org.example.exceptions.throwable.BadRequestException;
import org.example.util.GsonWrapper;
import org.food.features.shared.dto.CaloriesView;
import org.food.features.shared.dto.FoodView;
import org.food.features.shared.dto.NutrientView;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FoodUtils {

  private final ModelMapper modelMapper;
  private final GsonWrapper gsonWrapper;

  public void calculateFoodByAmount(FoodView food, Double size) {

    BigDecimal amount = BigDecimal.valueOf(size);

    if (food.getSize().compareTo(amount) == 0) {
      return;
    }

    BigDecimal multiplayer = amount.divide(food.getSize(), 20, RoundingMode.HALF_UP);

    food.setCalories(new CaloriesView(
        food.getCalories().amount().multiply(multiplayer).setScale(2, RoundingMode.HALF_UP))
    );
    food.setSize(amount);

    List<NutrientView> vitaminNutrients = new ArrayList<>();
    if (food.getVitaminNutrients() != null) {
      food.getVitaminNutrients().forEach(nutrient -> {

        NutrientView nutrientView = new NutrientView(
            nutrient.name(),
            nutrient.unit(),
            nutrient.amount().multiply(multiplayer).setScale(2, RoundingMode.HALF_UP)
        );
        vitaminNutrients.add(nutrientView);
      });
    }
    food.setVitaminNutrients(vitaminNutrients);
    List<NutrientView> mineralNutrients = new ArrayList<>();
    if (food.getMineralNutrients() != null) {
      food.getMineralNutrients().forEach(nutrient -> {

        NutrientView nutrientView = new NutrientView(
            nutrient.name(),
            nutrient.unit(),
            nutrient.amount().multiply(multiplayer).setScale(2, RoundingMode.HALF_UP)
        );
        mineralNutrients.add(nutrientView);
      });
    }
    food.setMineralNutrients(mineralNutrients);
    List<NutrientView> macronutrients = new ArrayList<>();
    if (food.getMacroNutrients() != null) {
      food.getMacroNutrients().forEach(nutrient -> {

        NutrientView nutrientView = new NutrientView(
            nutrient.name(),
            nutrient.unit(),
            nutrient.amount().multiply(multiplayer).setScale(2, RoundingMode.HALF_UP)
        );
        macronutrients.add(nutrientView);
      });
    }
    food.setMacroNutrients(macronutrients);
  }


  public String getUserId(String userToken) {
    try {
      return gsonWrapper.fromJson(userToken, UserView.class).id();
    } catch (Exception e) {
      throw new BadRequestException(INVALID_USER_TOKEN);
    }
  }
}
