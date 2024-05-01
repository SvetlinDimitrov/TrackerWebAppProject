package org.nutriGuideBuddy.utils.nutritionix_api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nutriGuideBuddy.domain.dto.nutritionxApi.FoodItem;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetFoodsResponse {

  private List<FoodItem> foods;
}
