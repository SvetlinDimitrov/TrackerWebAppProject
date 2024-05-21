package org.nutriGuideBuddy.utils;

import org.nutriGuideBuddy.domain.dto.meal.*;
import org.nutriGuideBuddy.domain.enums.AllowedCalorieUnits;
import org.nutriGuideBuddy.domain.enums.AllowedNutrients;
import org.nutriGuideBuddy.enums.Credentials;

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
        Credentials.VALID_FOOD_INFO_IMAGE.getValue()
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
        "Antidisestablishmentarianism's beauty may never be fully comprehended by the uninitiated, but those who delve deep into its labyrinthine depths will find themselves ensnared by its seductive allure, forever lost in a kaleidoscope of convoluted concepts and intricate ideologies that dance upon the precipice of comprehension.",
        "                 ",
        ""
    );
  }

  public static List<BigDecimal> getInvalidListOfInsertedCalorieAmount() {
    return List.of(
        BigDecimal.valueOf(-1),
        BigDecimal.valueOf(-9999999)
    );
  }

  public static List<ServingView> getInvalidServings() {
    return List.of(
        new ServingView(new BigDecimal(Credentials.VALID_FOOD_SERVING_AMOUNT.getValue()), new BigDecimal(Credentials.VALID_FOOD_SERVING_WEIGHT.getValue()), "Antidisestablishmentarianism's beauty may never be fully comprehended by the uninitiated, but those who delve deep into its labyrinthine depths will find themselves ensnared by its seductive allure, forever lost in a kaleidoscope of convoluted concepts and intricate ideologies that dance upon the precipice of comprehension."),
        new ServingView(new BigDecimal(Credentials.VALID_FOOD_SERVING_AMOUNT.getValue()), new BigDecimal(Credentials.VALID_FOOD_SERVING_WEIGHT.getValue()), ""),
        new ServingView(new BigDecimal(Credentials.VALID_FOOD_SERVING_AMOUNT.getValue()), new BigDecimal(Credentials.VALID_FOOD_SERVING_WEIGHT.getValue()), "              "),
        new ServingView(new BigDecimal(Credentials.VALID_FOOD_SERVING_AMOUNT.getValue()), null, Credentials.VALID_FOOD_SERVING_UNIT.getValue()),
        new ServingView(new BigDecimal(Credentials.VALID_FOOD_SERVING_AMOUNT.getValue()), BigDecimal.valueOf(0), Credentials.VALID_FOOD_SERVING_UNIT.getValue()),
        new ServingView(new BigDecimal(Credentials.VALID_FOOD_SERVING_AMOUNT.getValue()), BigDecimal.valueOf(-1), Credentials.VALID_FOOD_SERVING_UNIT.getValue()),
        new ServingView(null, new BigDecimal(Credentials.VALID_FOOD_SERVING_WEIGHT.getValue()), Credentials.VALID_FOOD_SERVING_UNIT.getValue()),
        new ServingView(BigDecimal.valueOf(0), new BigDecimal(Credentials.VALID_FOOD_SERVING_WEIGHT.getValue()), Credentials.VALID_FOOD_SERVING_UNIT.getValue()),
        new ServingView(BigDecimal.valueOf(-1), new BigDecimal(Credentials.VALID_FOOD_SERVING_WEIGHT.getValue()), Credentials.VALID_FOOD_SERVING_UNIT.getValue())
    );
  }

  public static List<FoodInfoView> getInvalidFoodInfoViews() {
    return List.of(
        new FoodInfoView("", Credentials.VALID_FOOD_INFO.getValue(), Credentials.VALID_FOOD_INFO_IMAGE.getValue()),
        new FoodInfoView("Antidisestablishmentarianism's beauty may never be fully comprehended by the uninitiated, but those who delve deep into its labyrinthine depths will find themselves ensnared by its seductive allure, forever lost in a kaleidoscope of convoluted concepts and intricate ideologies that dance upon the precipice of comprehension.", Credentials.VALID_FOOD_INFO.getValue(), Credentials.VALID_FOOD_INFO_IMAGE.getValue()),
        new FoodInfoView("     ", Credentials.VALID_FOOD_INFO.getValue(), Credentials.VALID_FOOD_INFO_IMAGE.getValue()),
        new FoodInfoView(Credentials.VALID_FOOD_INFO.getValue(), "", Credentials.VALID_FOOD_INFO_IMAGE.getValue()),
        new FoodInfoView(Credentials.VALID_FOOD_INFO.getValue(), "     ", Credentials.VALID_FOOD_INFO_IMAGE.getValue()),
        new FoodInfoView(Credentials.VALID_FOOD_INFO.getValue(), Credentials.VALID_FOOD_INFO.getValue(), "     "),
        new FoodInfoView(Credentials.VALID_FOOD_INFO.getValue(), Credentials.VALID_FOOD_INFO.getValue(), "    asdasdasd "),
        new FoodInfoView(Credentials.VALID_FOOD_INFO.getValue(), Credentials.VALID_FOOD_INFO.getValue(), "sss   asdasdasd "),
        new FoodInfoView(Credentials.VALID_FOOD_INFO.getValue(), Credentials.VALID_FOOD_INFO.getValue(), ". "),
        new FoodInfoView(Credentials.VALID_FOOD_INFO.getValue(), Credentials.VALID_FOOD_INFO.getValue(), "https://regexr.com/3g1v7"),
        new FoodInfoView(Credentials.VALID_FOOD_INFO.getValue(), Credentials.VALID_FOOD_INFO.getValue(), "https://soundcloud.com/discover")
    );
  }

  public static List<NutritionView> getInvalidNutrientViews() {
    return List.of(
        new NutritionView(AllowedNutrients.VitaminC.getNutrientName(), null, BigDecimal.ONE),
        new NutritionView(null, AllowedNutrients.VitaminC.getNutrientUnit(), BigDecimal.ONE),
        new NutritionView(AllowedNutrients.VitaminC.getNutrientName(), AllowedNutrients.VitaminC.getNutrientUnit(), null),
        new NutritionView(AllowedNutrients.VitaminC.getNutrientName(), AllowedNutrients.VitaminC.getNutrientUnit(), BigDecimal.valueOf(-1)),
        new NutritionView(AllowedNutrients.VitaminC.getNutrientName(), AllowedNutrients.VitaminC.getNutrientUnit(), BigDecimal.valueOf(0)),
        new NutritionView(AllowedNutrients.VitaminC.getNutrientName(), AllowedNutrients.VitaminC.getNutrientUnit(), BigDecimal.valueOf(-99999)),
        new NutritionView("VitaminC", AllowedNutrients.VitaminC.getNutrientUnit(), BigDecimal.valueOf(1)),
        new NutritionView("", AllowedNutrients.VitaminC.getNutrientUnit(), BigDecimal.valueOf(1)),
        new NutritionView("Antidisestablishmentarianism's beauty may never be fully comprehended by the uninitiated, but those who delve deep into its labyrinthine depths will find themselves ensnared by its seductive allure, forever lost in a kaleidoscope of convoluted concepts and intricate ideologies that dance upon the precipice of comprehension.", AllowedNutrients.VitaminC.getNutrientUnit(), BigDecimal.valueOf(1)),
        new NutritionView("CATS", AllowedNutrients.VitaminC.getNutrientUnit(), BigDecimal.valueOf(1)),
        new NutritionView(AllowedNutrients.VitaminC.getNutrientName(), "", BigDecimal.valueOf(1)),
        new NutritionView(AllowedNutrients.VitaminC.getNutrientName(), "Antidisestablishmentarianism's beauty may never be fully comprehended by the uninitiated, but those who delve deep into its labyrinthine depths will find themselves ensnared by its seductive allure, forever lost in a kaleidoscope of convoluted concepts and intricate ideologies that dance upon the precipice of comprehension.", BigDecimal.valueOf(1)),
        new NutritionView(AllowedNutrients.VitaminC.getNutrientName(), "cats", BigDecimal.valueOf(1)),
        new NutritionView(AllowedNutrients.VitaminC.getNutrientName(), "g", BigDecimal.valueOf(1)),
        new NutritionView(AllowedNutrients.VitaminC.getNutrientName(), "l", BigDecimal.valueOf(1)),
        new NutritionView(AllowedNutrients.VitaminC.getNutrientName(), "ug", BigDecimal.valueOf(1))
    );
  }

  public static List<String> getFoodsToSearch() {
    return Arrays.asList(
        "Spaghetti Bolognese",
        "Sushi",
        "Chicken Tikka Masala",
        "Caesar Salad",
        "Cheeseburger",
        "Pad Thai",
        "Margherita Pizza",
        "Lasagna",
        "Hamburger",
        "Pho",
        "Tacos",
        "Ramen",
        "Steak",
        "Pasta Carbonara",
        "Grilled Cheese Sandwich",
        "Burrito",
        "Fried Rice",
        "Fish and Chips",
        "Mushroom Risotto",
        "Chicken Curry"
    );
  }
}
