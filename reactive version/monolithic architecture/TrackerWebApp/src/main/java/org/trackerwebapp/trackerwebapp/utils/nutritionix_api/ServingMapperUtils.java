package org.trackerwebapp.trackerwebapp.utils.nutritionix_api;

import org.trackerwebapp.trackerwebapp.domain.dto.meal.ServingView;
import org.trackerwebapp.trackerwebapp.domain.dto.nutritionxApi.FoodItem;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ServingMapperUtils {

  public static List<ServingView> getServings(FoodItem dto) {
    //TODO: MAKE IT THAT THE FIRST VALUE THAT APPEARS TO BE THE MAIN SERVING (BRANDED FOODS ARE DIFFERENT THEN NORMAL)

    List<ServingView> servings = dto.getMeasures()
        .stream()
        .map(measure -> new ServingView(measure.getQty(), measure.getServingWeight(), measure.getMeasure()))
        .toList();

    return servings;
  }
}
