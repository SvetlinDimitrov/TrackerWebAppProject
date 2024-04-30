package org.trackerwebapp.trackerwebapp.utils.nutritionix_api;

import org.trackerwebapp.trackerwebapp.domain.dto.meal.FoodInfoView;
import org.trackerwebapp.trackerwebapp.domain.dto.nutritionxApi.FoodItem;
import org.trackerwebapp.trackerwebapp.domain.dto.nutritionxApi.Photo;
import org.trackerwebapp.trackerwebapp.domain.dto.nutritionxApi.Tags;

import java.util.Optional;

public class FoodInfoMapperUtils {

  public static FoodInfoView generateFoodInfo(FoodItem foodItem) {
    return Optional.ofNullable(foodItem.getBrandName())
        .map(brandName ->
            new FoodInfoView(brandName,
                foodItem.getNfIngredientStatement(),
                getPicture(foodItem))
        )
        .orElse(
            new FoodInfoView(Optional.ofNullable(foodItem.getTags())
                .map(Tags::getItem)
                .orElse(null),
                foodItem.getNfIngredientStatement(),
                getPicture(foodItem)));
  }

  private static String getPicture(FoodItem foodItem) {
    return Optional.ofNullable(foodItem.getPhoto())
        .map(Photo::getThumb)
        .orElse(null);
  }
}
