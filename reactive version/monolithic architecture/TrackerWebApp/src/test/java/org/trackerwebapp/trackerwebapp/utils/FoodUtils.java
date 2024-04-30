package org.trackerwebapp.trackerwebapp.utils;

import org.trackerwebapp.trackerwebapp.domain.dto.meal.*;
import org.trackerwebapp.trackerwebapp.domain.enums.AllowedCalorieUnits;
import org.trackerwebapp.trackerwebapp.domain.enums.AllowedNutrients;
import org.trackerwebapp.trackerwebapp.enums.Credentials;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class FoodUtils {

  public static InsertFoodDto createInsertedFood(String name, CalorieView calorieView, ServingView servingView, FoodInfoView foodInfoView, List<ServingView> servingViewList, List<NutritionView> nutritionViewList) {
    return new InsertFoodDto(name, calorieView, servingView, foodInfoView, servingViewList, nutritionViewList);
  }

  public static CalorieView createValidCalorieView() {
    return new CalorieView(BigDecimal.valueOf(366), AllowedCalorieUnits.CALORIE.getSymbol());
  }

  public static ServingView createValidServingView() {
    return new ServingView(
        new BigDecimal(Credentials.VALID_FOOD_SERVING_AMOUNT.getValue()),
        new BigDecimal(Credentials.VALID_FOOD_SERVING_WEIGHT.getValue()),
        Credentials.VALID_FOOD_SERVING_UNIT.getValue()
    );
  }

  public static FoodInfoView createValidFoodInfoView() {
    return new FoodInfoView(
        Credentials.VALID_FOOD_INFO.getValue(),
        Credentials.VALID_FOOD_INFO.getValue(),
        Credentials.VALID_FOOD_INFO.getValue()
    );
  }

  public static InsertFoodDto createValidInsertedFoodWithEmptyNutritions() {
    return createInsertedFood(
        Credentials.VALID_MEAL_NAME.getValue(),
        createValidCalorieView(),
        createValidServingView(),
        createValidFoodInfoView(),
        List.of(),
        List.of()
    );
  }

  public static InsertFoodDto createValidInsertedFoodWithEveryPossibleNutrientView() {

    List<NutritionView> nutritionViews = Arrays.stream(AllowedNutrients.values())
        .map(data -> new NutritionView(data.getNutrientName(), data.getNutrientUnit(), BigDecimal.ONE))
        .toList();

    return createInsertedFood(
        Credentials.VALID_MEAL_NAME.getValue(),
        createValidCalorieView(),
        createValidServingView(),
        createValidFoodInfoView(),
        List.of(),
        nutritionViews
    );
  }

  public static List<String> getInvalidListOfInsertedFoodNames() {

    //MIN 2 chars are required
    return List.of(
        "s",
        "                 ",
        "        s     ",
        " !"
    );
  }

  public static List<BigDecimal> getInvalidListOfInsertedCalorieAmount() {
    return List.of(
        BigDecimal.valueOf(0),
        BigDecimal.valueOf(0.99999),
        BigDecimal.valueOf(-1),
        BigDecimal.valueOf(-9999999)
    );
  }

  public static List<String> getInvalidUnitNames() {
    return List.of(
        "l",
        "      ",
        "liters",
        "mg"
    );
  }

  public static List<BigDecimal> getInvalidFoodAmounts() {
    return List.of(
        BigDecimal.valueOf(-1),
        BigDecimal.valueOf(-0.99999999),
        BigDecimal.valueOf(0.99999999),
        BigDecimal.valueOf(0)
    );
  }

  public static List<NutritionView> getInvalidNutrientViews() {
    return List.of(
        new NutritionView(AllowedNutrients.VitaminC.getNutrientName(), null, BigDecimal.ONE),
        new NutritionView(null, AllowedNutrients.VitaminC.getNutrientUnit(), BigDecimal.ONE),
        new NutritionView(AllowedNutrients.VitaminC.getNutrientName(), AllowedNutrients.VitaminC.getNutrientUnit(), null),
        new NutritionView(AllowedNutrients.VitaminC.getNutrientName(), AllowedNutrients.VitaminC.getNutrientUnit(), BigDecimal.valueOf(0.9999)),
        new NutritionView(AllowedNutrients.VitaminC.getNutrientName(), AllowedNutrients.VitaminC.getNutrientUnit(), BigDecimal.valueOf(0)),
        new NutritionView(AllowedNutrients.VitaminC.getNutrientName(), AllowedNutrients.VitaminC.getNutrientUnit(), BigDecimal.valueOf(-1)),
        new NutritionView(AllowedNutrients.VitaminC.getNutrientName(), AllowedNutrients.VitaminC.getNutrientUnit(), BigDecimal.valueOf(-99999)),
        new NutritionView("VitaminC", AllowedNutrients.VitaminC.getNutrientUnit(), BigDecimal.valueOf(1)),
        new NutritionView("", AllowedNutrients.VitaminC.getNutrientUnit(), BigDecimal.valueOf(1)),
        new NutritionView("CATS", AllowedNutrients.VitaminC.getNutrientUnit(), BigDecimal.valueOf(1)),
        new NutritionView(AllowedNutrients.VitaminC.getNutrientName(), "", BigDecimal.valueOf(1)),
        new NutritionView(AllowedNutrients.VitaminC.getNutrientName(), "cats", BigDecimal.valueOf(1)),
        new NutritionView(AllowedNutrients.VitaminC.getNutrientName(), "g", BigDecimal.valueOf(1)),
        new NutritionView(AllowedNutrients.VitaminC.getNutrientName(), "l", BigDecimal.valueOf(1)),
        new NutritionView(AllowedNutrients.VitaminC.getNutrientName(), "ug", BigDecimal.valueOf(1))
    );
  }

}
