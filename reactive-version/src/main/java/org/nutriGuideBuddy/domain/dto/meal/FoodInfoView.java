package org.nutriGuideBuddy.domain.dto.meal;

import org.nutriGuideBuddy.domain.entity.FoodInfoEntity;

public record FoodInfoView(
    String info,
    String largeInfo,
    String picture
) {

  public static FoodInfoView toView(FoodInfoEntity foodInfoView) {
    return new FoodInfoView(foodInfoView.getInfo() , foodInfoView.getLargeInfo() , foodInfoView.getPicture());
  }
}
