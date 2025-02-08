package org.gateway.filter.food;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FOOD_PATHS {
  EMBEDDED_BRANDED_FOODS("/api/v1/food/embedded/brandedFoods"),
  SURVEY_FOODS("/api/v1/food/embedded/surveyFoods"),
  FINAL_FOODS("/api/v1/food/embedded/finalFoods"),
  CUSTOM_FOODS("/api/v1/food/custom");

  public final String path;
}
